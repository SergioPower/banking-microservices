package com.banking.transfer.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

     // RestClient CON LoadBalanced (para llamar a otros servicios por nombre)
    @Bean("loadBalancedRestClientBuilder")
    @LoadBalanced
    public RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }

    // RestClient SIN LoadBalanced (para Eureka y otras llamadas directas)
    @Bean("simpleRestClientBuilder")
    @Primary  // ← Este será el bean por defecto
    public RestClient.Builder simpleRestClientBuilder() {
        return RestClient.builder();
    }
}
