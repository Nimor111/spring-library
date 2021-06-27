package com.example.book.client;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class StoreClientImpl implements StoreClient {
    private RestTemplate restTemplate;
    private Environment env;

    public StoreClientImpl(RestTemplate restTemplate, Environment env) {
        this.restTemplate = restTemplate;
        this.env = env;
    }

    @Override
    public StoreDTO getStoreById(Long id) {
        return restTemplate.getForObject(
                Objects.requireNonNull(env.getProperty("store-service.url")) + "/" + id, StoreDTO.class);
    }
}
