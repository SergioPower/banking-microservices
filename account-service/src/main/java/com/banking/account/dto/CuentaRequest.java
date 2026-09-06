package com.banking.account.dto;

import java.math.BigDecimal;

import com.banking.account.entity.TipoCuenta;
import com.fasterxml.jackson.annotation.JsonCreator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CuentaRequest(
    @NotBlank String numeroCuenta,

    @NotBlank
    String titular,

    @NotNull
    TipoCuenta tipoCuenta,

    @NotNull
    @PositiveOrZero
    BigDecimal saldoInicial
) {
    @JsonCreator 
    public CuentaRequest{

    }
}
