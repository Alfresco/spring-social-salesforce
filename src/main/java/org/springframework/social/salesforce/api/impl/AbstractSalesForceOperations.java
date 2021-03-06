package org.springframework.social.salesforce.api.impl;

import org.springframework.social.ApiBinding;
import org.springframework.social.MissingAuthorizationException;
import org.springframework.social.salesforce.api.Salesforce;

import static org.springframework.social.salesforce.connect.SalesforceServiceProvider.ID;


/**
 * @author Umut Utkan
 * @author Jared Ottley
 */
public class AbstractSalesForceOperations<T extends ApiBinding> {

    protected T api;


    public AbstractSalesForceOperations(T api) {
        this.api = api;
    }

    protected void requireAuthorization() {
        if (!api.isAuthorized()) {
            throw new MissingAuthorizationException(ID);
        }
    }

    protected static String BASE_URL = "https://na7.salesforce.com/services/data";

    public String getVersion()
    {
        return ((Salesforce)api).apiOperations().getVersion();
    }

}
