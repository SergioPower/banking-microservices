package com.banking.transfer.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CuentaResponse(
        Long id,
        String numeroCuenta,
        String titular,
        String tipoCuenta,
        BigDecimal saldo,
        Boolean activa,
        LocalDateTime fechaCreacion
) {
}