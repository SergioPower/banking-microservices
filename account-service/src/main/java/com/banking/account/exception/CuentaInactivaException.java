package com.banking.account.exception;

public class CuentaInactivaException extends RuntimeException {

    public CuentaInactivaException(String numeroCuenta){
        super("La cuenta "+ numeroCuenta + " se encuentra inactiva");
    }
}
