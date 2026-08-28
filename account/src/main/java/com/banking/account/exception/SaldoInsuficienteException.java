package com.banking.account.exception;

import java.math.BigDecimal;

public class SaldoInsuficienteException extends RuntimeException{

    public SaldoInsuficienteException(BigDecimal saldoActual) {
        super("Saldo insuficiente, su saldo actual es de: " + saldoActual);
    }
    
}
