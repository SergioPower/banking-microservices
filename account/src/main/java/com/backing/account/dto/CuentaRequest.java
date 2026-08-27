package com.backing.account.dto;

import java.math.BigDecimal;

import com.backing.account.entity.TipoCuenta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class CuentaRequest {
    
    @NotBlank
    String numeroCuenta;

    @NotBlank
    String titular;
    
    @NotBlank
    TipoCuenta tipoCuenta;

    @NotBlank
    @PositiveOrZero
    BigDecimal saldoInicial;

}
