package com.banking.transfer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    public RestClient restClient() {
        return RestClient.builder()
            .baseUrl("http://localhost8081")
            .build();
    }
}
