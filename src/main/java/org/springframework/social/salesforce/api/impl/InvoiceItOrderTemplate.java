package org.springframework.social.salesforce.api.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.social.salesforce.api.InvoiceItOperations;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;

public class InvoiceItOrderTemplate extends AbstractSalesForceOperations<Salesforce> implements InvoiceItOperations {

    public static final String INVOICE_IT_ORDERS_API_URI = "/services/apexrest/Invoice_API/invoiceit/orders";
    private RestTemplate restTemplate;

    public InvoiceItOrderTemplate(Salesforce api, RestTemplate restTemplate) {
        super(api);
        this.restTemplate = restTemplate;

    }

    @Override
    @SuppressWarnings("rawtypes")
    public Map<String, Object> createOrder(Map<String, Object> fields) {
        requireAuthorization();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Map> entity = new HttpEntity<Map>(fields, headers);

        restTemplate.getMessageConverters().add(new InvoiceItHttpMessageConverter());

        Map<String, Object> map = restTemplate.postForObject(api.getInstanceUrl() + INVOICE_IT_ORDERS_API_URI, entity, Map.class);
        return map;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
