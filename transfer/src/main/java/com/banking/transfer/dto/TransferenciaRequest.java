package com.banking.transfer.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransferenciaRequest(
    @NotNull Long cuentaOrigenId,

    @NotNull Long cuentaDestinoId,
    
    @NotNull @Positive BigDecimal monto,

    String referencia

) {
    @JsonCreator
    public TransferenciaRequest {
    }
    

}
