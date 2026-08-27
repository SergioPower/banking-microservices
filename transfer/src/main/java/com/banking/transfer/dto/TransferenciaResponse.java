package com.banking.transfer.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.banking.transfer.entity.EstadoTransferencia;

public record TransferenciaResponse(
    Long id,
    Long cuentaOrigenId,
    Long cuentaDestinoId,
    BigDecimal monto,
    EstadoTransferencia estado,
    LocalDateTime fechaCreacion,
    String referencia

) {

}
