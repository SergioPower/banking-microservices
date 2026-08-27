package com.backing.account.exception;

public class CuentaNotFoundException extends RuntimeException {

    public CuentaNotFoundException() {
        super("Cuenta no encontrada");
    }
    

}
