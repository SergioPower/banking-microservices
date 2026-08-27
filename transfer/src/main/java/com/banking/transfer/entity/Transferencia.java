package com.banking.transfer.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;

@Entity
public class Transferencia {
    private Long id;
    private Long cuentaOrigenId;
    private Long cuentaDestinoId;
    private BigDecimal monto;
    private EstadoTransferencia estado;
    private LocalDateTime fechaCreacion;
    private String referencia;


}
