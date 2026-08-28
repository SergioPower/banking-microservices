package com.banking.transfer.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransferenciaRequest(
    @NotNull(message = "El ID de la cuenta origen es obligatorio") 
    Long cuentaOrigenId,

    @NotNull(message = "El ID de la cuenta destino es obligatorio")
    Long cuentaDestinoId,
    
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    BigDecimal monto,

    String referencia

) {
    @JsonCreator
    public TransferenciaRequest {
    }
    

}
