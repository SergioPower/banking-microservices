package com.banking.notification.event;

import java.math.BigDecimal;

public record TransferenciaCompletadaEvent(
        Long transferenciaId,
        Long cuentaOrigenId,
        Long cuentaDestinoId,
        BigDecimal monto,
        String estado
) {
}