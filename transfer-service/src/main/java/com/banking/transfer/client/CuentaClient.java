package com.banking.transfer.client;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.banking.transfer.dto.CuentaResponse;
import com.banking.transfer.dto.MontoRequest;

@Component
public class CuentaClient {

    private final RestClient restClient;

    public CuentaClient(
            @Qualifier("loadBalancedRestClientBuilder")
            RestClient.Builder builder) {

        this.restClient = builder
                .baseUrl("http://ACCOUNT-SERVICE/api/cuentas")
                .build();
    }

    public CuentaResponse obtenerCuenta(Long id) {
        return restClient.get()
                .uri("/{id}", id)
                .retrieve()
                .body(CuentaResponse.class);
    }

    public CuentaResponse retirar(Long id, BigDecimal monto) {
        return restClient.post()
                .uri("/{id}/retiros", id)
                .body(new MontoRequest(monto))
                .retrieve()
                .body(CuentaResponse.class);
    }

    public CuentaResponse depositar(Long id, BigDecimal monto) {
        return restClient.post()
                .uri("/{id}/depositos", id)
                .body(new MontoRequest(monto))
                .retrieve()
                .body(CuentaResponse.class);
    }
}
