package com.banking.transfer.client;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.banking.transfer.dto.CuentaResponse;
import com.banking.transfer.dto.MontoRequest;

@Component
public class CuentaClient {
    
    private final RestClient restClient;

    public CuentaClient(RestClient restClient){
        this.restClient = restClient;
    }

    public CuentaResponse obtenerCuenta(Long id){
        return restClient.get()
            .uri("/api/cuentas/{id}", id)
            .retrieve()
            .body(CuentaResponse.class);
    }

    public CuentaResponse retirar(Long id, BigDecimal monto){
        MontoRequest request = new MontoRequest(monto);
        
        return restClient.post()
                .uri("/api/cuentas/{id}/retiros", id)
                .body(request)
                .retrieve()
                .body(CuentaResponse.class);

    }

    public CuentaResponse depositar(Long id, BigDecimal monto){
        MontoRequest request = new MontoRequest(monto);

        return restClient.post()
                .uri("/api/cuentas/{id}/depositos", id)
                .body(request)
                .retrieve()
                .body(CuentaResponse.class);
    }



}
