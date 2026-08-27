package com.banking.transfer.dto;

import java.math.BigDecimal;

import com.banking.transfer.validation.CuentasDistintas;
import com.fasterxml.jackson.annotation.JsonCreator;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@CuentasDistintas(message = "La cuenta origen y la cuenta destino deber ser diferentes")
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
