package org.springframework.social.salesforce.api;


/**
 * Created by jottley on 2/8/17.
 */
public class InvalidSalesforceApiVersionException
        extends Throwable
{
    public InvalidSalesforceApiVersionException()
    {
    }

    public InvalidSalesforceApiVersionException(String version)
    {
        super(version + " is not a valid Salesforce Api version.");
    }
}
