package org.springframework.social.salesforce.api.impl;

import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.social.salesforce.api.InvalidSalesforceApiVersionException;
import org.springframework.social.salesforce.api.SObjectDetail;
import org.springframework.social.salesforce.api.SObjectSummary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

/**
 * @author Umut Utkan
 */
public class SObjectsTemplateTest extends AbstractSalesforceTest {

    @Test
    public void getSObjects() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/" + salesforce.apiOperations().getVersion() + "/sobjects"))
                .andExpect(method(GET))
                .andRespond(withStatus(OK).body(loadResource("sobjects.json")).headers(responseHeaders));
        List<Map> sobjects = salesforce.sObjectsOperations().getSObjects();
        assertEquals(160, sobjects.size());
        assertEquals("Account", sobjects.get(0).get("name"));
        assertEquals("Account", sobjects.get(0).get("label"));
        assertEquals("Accounts", sobjects.get(0).get("labelPlural"));
        assertEquals("/services/data/v37.0/sobjects/Account", ((Map) sobjects.get(0).get("urls")).get("sobject"));
    }

    @Test
    public void getSObject() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/" + salesforce.apiOperations().getVersion() + "/sobjects/Account"))
                .andExpect(method(GET))
                .andRespond(withStatus(OK).body(loadResource("account.json")).headers(responseHeaders));
        SObjectSummary account = salesforce.sObjectsOperations().getSObjectSummary("Account");
        assertNotNull(account);
        assertEquals("Account", account.getName());
        assertEquals("Account", account.getLabel());
        assertEquals(true, account.isUndeletable());
        assertEquals("001", account.getKeyPrefix());
        assertEquals(false, account.isCustom());
        assertEquals("/services/data/v37.0/sobjects/Account/{ID}", account.getUrls().get("rowTemplate"));
    }

    @Test
    public void getSObject2() {
        try
        {
            salesforce.apiOperations().setVersion("v38.0");
            mockServer.expect(requestTo(
                    "https://na7.salesforce.com/services/data/" + salesforce.apiOperations().getVersion() + "/sobjects/Account"))
                      .andExpect(method(GET))
                      .andRespond(withStatus(OK).body(loadResource("account2.json")).headers(responseHeaders));
            SObjectSummary account = salesforce.sObjectsOperations().getSObjectSummary("Account");
            assertNotNull(account);
            assertEquals("Account", account.getName());
            assertEquals("Account", account.getLabel());
            assertEquals(true, account.isUndeletable());
            assertEquals("001", account.getKeyPrefix());
            assertEquals(false, account.isCustom());
            assertEquals("/services/data/v38.0/sobjects/Account/{ID}", account.getUrls().get("rowTemplate"));
        }
        catch (InvalidSalesforceApiVersionException e)
        {
            fail("InvalidSalesforceApiVersionException thrown");
        }
    }

    @Test
    public void describeSObject() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/" + salesforce.apiOperations().getVersion() + "/sobjects/Account/describe"))
                .andExpect(method(GET))
                .andRespond(withStatus(OK).body(loadResource("account_desc.json")).headers(responseHeaders));
        SObjectDetail account = salesforce.sObjectsOperations().describeSObject("Account");
        assertNotNull(account);
        assertEquals("Account", account.getName());
        assertEquals("Account", account.getLabel());
        assertEquals(45, account.getFields().size());
        assertEquals("Id", account.getFields().get(0).getName());
        assertEquals(1, account.getRecordTypeInfos().size());
        assertEquals("Master", account.getRecordTypeInfos().get(0).getName());
        assertEquals(36, account.getChildRelationships().size());
        assertEquals("ParentId", account.getChildRelationships().get(0).getField());
    }

    @Test
    public void getBlob() throws IOException {
        responseHeaders.remove("content-type");
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/" + salesforce.apiOperations().getVersion() + "/sobjects/Account/xxx/avatar"))
                .andExpect(method(GET))
                .andRespond(withStatus(OK).body(new ByteArrayResource("does-not-matter".getBytes("UTF-8"))).headers(responseHeaders));
        BufferedReader reader = new BufferedReader(new InputStreamReader(salesforce.sObjectsOperations().getBlob("Account", "xxx", "avatar")));
        assertEquals("does-not-matter", reader.readLine());
    }

    @Test
    public void testCreate() throws IOException {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/" + salesforce.apiOperations().getVersion() + "/sobjects/Lead"))
                .andExpect(method(POST))
                .andRespond(withStatus(OK).body(new ByteArrayResource("{\"Id\" : \"1234\"}".getBytes("UTF-8"))).headers(responseHeaders));
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("LastName", "Doe");
        fields.put("FirstName", "John");
        fields.put("Company", "Acme, Inc.");
        Map<?, ?> result = salesforce.sObjectsOperations().create("Lead", fields);
        assertEquals(1, result.size());
        assertEquals("1234", result.get("Id"));
    }

    @Test
    public void testUpdate() throws IOException {
        // salesforce returns an empty body with a success code if no failures.
        // But, have to mock a json string to satisfy Mock Rest service.
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/" + salesforce.apiOperations().getVersion() + "/sobjects/Lead/abc123?_HttpMethod=PATCH"))
                .andExpect(method(POST))
                .andRespond(withStatus(OK).body("{}").headers(responseHeaders));
        Map<String, Object> leadData = new HashMap<String, Object>();
        leadData.put("LastName", "Doe");
        leadData.put("FirstName", "John");
        leadData.put("Company", "Acme, Inc.");
        Map<?, ?> result = salesforce.sObjectsOperations().update("Lead", "abc123", leadData);
        assertTrue(result.size() == 0);
    }
    
    @Test
    public void testDelete() throws IOException {
        // salesforce returns an empty body with a success code if no failures.
        // But, have to mock a json string to satisfy Mock Rest service.
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/" + salesforce.apiOperations().getVersion() + "/sobjects/Lead/abc123"))
                .andExpect(method(DELETE))
                .andRespond(withStatus(OK).body("{}").headers(responseHeaders));
        salesforce.sObjectsOperations().delete("Lead", "abc123");
        //if it makes it here with no error then it is good.
        assertTrue(true);
    }

}
