package org.springframework.social.salesforce.api.impl;

import org.junit.Test;
import org.springframework.social.salesforce.api.ApiVersion;
import org.springframework.social.salesforce.api.InvalidSalesforceApiVersionException;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.social.test.client.RequestMatchers.method;
import static org.springframework.social.test.client.RequestMatchers.requestTo;
import static org.springframework.social.test.client.ResponseCreators.withResponse;

/**
 * @author Umut Utkan
 */
public class MetaApiTemplateTest extends AbstractSalesforceTest {

    @Test
    public void getApiVersions() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data"))
                .andExpect(method(GET))
                .andRespond(withResponse(loadResource("versions.json"), responseHeaders));
        List<ApiVersion> versions = salesforce.apiOperations().getVersions();
        assertEquals(4, versions.size());
        assertEquals("Winter '12", versions.get(3).getLabel());
        assertEquals("23.0", versions.get(3).getVersion());
        assertEquals("/services/data/v23.0", versions.get(3).getUrl());
    }

    @Test
         public void getServices() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/v23.0"))
                  .andExpect(method(GET))
                  .andRespond(withResponse(loadResource("services.json"), responseHeaders));
        Map<String, String> services = salesforce.apiOperations().getServices("v23.0");
        assertEquals(6, services.size());
        assertEquals("/services/data/v23.0/sobjects", services.get("sobjects"));
        assertEquals("/services/data/v23.0/chatter", services.get("chatter"));
    }

    @Test
    public void getServices2() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/v37.0"))
                  .andExpect(method(GET))
                  .andRespond(withResponse(loadResource("services2.json"), responseHeaders));
        Map<String, String> services = salesforce.apiOperations().getServices();
        assertEquals(6, services.size());
        assertEquals("/services/data/v37.0/sobjects", services.get("sobjects"));
        assertEquals("/services/data/v37.0/chatter", services.get("chatter"));
    }

    @Test
    public void getVersion()
    {
        String version = salesforce.apiOperations().getVersion();
        assertEquals("v37.0", version);
    }

    @Test
    public void setVersion()
    {
        try
        {
            salesforce.apiOperations().setVersion("v38.0");
            String version = salesforce.apiOperations().getVersion();
            assertEquals("v38.0", version);
        }
        catch (InvalidSalesforceApiVersionException e)
        {
            fail("InvalidSalesforceApiVersionException thrown");
        }
    }

    @Test
    public void setVersion2()
    {
        try
        {
            salesforce.apiOperations().setVersion("38.0");
            fail("InvalidSalesforceApiVersionException not thrown");
        }
        catch (InvalidSalesforceApiVersionException e)
        {
            assertEquals("38.0 is not a valid Salesforce Api version.", e.getMessage());
        }
    }

}
