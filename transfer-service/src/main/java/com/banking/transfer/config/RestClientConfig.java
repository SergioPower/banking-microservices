package com.banking.transfer.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    // RestClient con LoadBalanced para comunicarse con otros servicios
    @Bean("loadBalancedRestClient")
    @LoadBalanced
    public RestClient loadBalancedRestClient() {
        return RestClient.builder().build();
    }

    // RestClient sin LoadBalanced para comunicarse con Eureka
    @Bean("simpleRestClient")
    @Primary
    public RestClient simpleRestClient() {
        return RestClient.builder().build();
    }
}