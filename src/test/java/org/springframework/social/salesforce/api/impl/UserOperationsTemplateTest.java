package org.springframework.social.salesforce.api.impl;

import org.junit.Test;
import org.springframework.social.salesforce.api.SalesforceUserDetails;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

public class UserOperationsTemplateTest extends AbstractSalesforceTest {

    @Test
    public void getUserDetails() {
        mockServer.expect(requestTo("https://login.salesforce.com/services/oauth2/userinfo"))
                .andExpect(method(GET))
                .andRespond(withStatus(OK).body(loadResource("userDetails.json")).headers(responseHeaders));
        SalesforceUserDetails userDetails = salesforce.userOperations().getSalesforceUserDetails();
        assertEquals("John Doe", userDetails.getName());
        assertEquals("john@doe.com", userDetails.getEmail());
        assertEquals("12345", userDetails.getId());
        assertEquals("johnny", userDetails.getPreferredUsername());
        assertEquals("John", userDetails.getFirstName());
        assertEquals("Doe", userDetails.getLastName());
    }
}
