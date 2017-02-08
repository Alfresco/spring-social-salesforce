package org.springframework.social.salesforce.api;

import java.util.List;
import java.util.Map;

/**
 * Defines operations for getting info on the API.
 *
 * @author Umut Utkan
 */
public interface ApiOperations {

    static final String DEFAULT_API_VERSION = "v37.0";

    List<ApiVersion> getVersions();

    Map<String, String> getServices(String version);

    Map<String, String> getServices();

    void setVersion(String version)
            throws InvalidSalesforceApiVersionException;

    String getVersion();
}
