package org.springframework.social.salesforce.api.impl;

import org.springframework.social.salesforce.api.ApexRestOperations;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.support.URIBuilder;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

public class ApexRestTemplate extends AbstractSalesForceOperations<Salesforce> implements ApexRestOperations {

    private static final String APEX_REST_PATH = "/services/apexrest/";
    private static final String PATH_SEPARATOR = "/";

    private final RestTemplate restTemplate;

    public ApexRestTemplate(Salesforce api, RestTemplate restTemplate) {
        super(api);
        this.restTemplate = restTemplate;
    }

    @Override
    public <T> T get(String resource, String identifier, Class<T> resultType) {
        Assert.isTrue(isNotBlank(resource), "Resource must not be blank");
        Assert.isTrue(isNotBlank(identifier), "Identifier must not be blank");
        Assert.notNull(resultType, "Result type must not be null");

        URI uri = URIBuilder.fromUri(api.getInstanceUrl() + APEX_REST_PATH + resource + PATH_SEPARATOR + identifier).build();
        return restTemplate.getForObject(uri, resultType);
    }
}
