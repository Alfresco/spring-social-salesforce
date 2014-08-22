package org.springframework.social.salesforce.api;

public interface ApexRestOperations {

    <T> T get(String resource, String identifier, Class<T> resultType);
}
