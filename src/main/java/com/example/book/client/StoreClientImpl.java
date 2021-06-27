package com.example.book.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class StoreClientImpl implements StoreClient {
    private RestTemplate restTemplate;

    private final String url;

    public StoreClientImpl(RestTemplate restTemplate, @Value("${store-service.url}") String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    @Override
    public StoreDTO getStoreById(Long id) {
        return restTemplate.getForObject(url + "/" + id, StoreDTO.class);
    }
}
