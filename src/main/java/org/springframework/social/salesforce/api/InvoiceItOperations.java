package org.springframework.social.salesforce.api;

import java.util.Map;

public interface InvoiceItOperations {

    public Map<String, Object> createOrder(Map<String, Object> fields);

}
