package org.springframework.social.salesforce.api.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.social.salesforce.api.Community;
import org.springframework.social.salesforce.api.CommunityOperations;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class CommunityTemplate extends AbstractSalesForceOperations<Salesforce> implements CommunityOperations {

    private RestTemplate restTemplate;

    public CommunityTemplate(Salesforce api, RestTemplate restTemplate) {
        super(api);
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Community> getCommunities() {

        requireAuthorization();

        JsonNode dataNode = restTemplate.getForObject(api.getBaseUrl() + "/" + getVersion() + "/connect/communities", JsonNode.class, getVersion());

        return api.readList(dataNode.get("communities"), Community.class);
    }

}
