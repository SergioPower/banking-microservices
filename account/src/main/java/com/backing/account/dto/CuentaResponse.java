package com.backing.account.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.backing.account.entity.TipoCuenta;

public record CuentaResponse (
    Long id,
    String numeroCuenta,
    String titular,
    TipoCuenta tipoCuenta,
    BigDecimal saldo,
    Boolean activa,
    LocalDateTime fechaCreacion

) {}
