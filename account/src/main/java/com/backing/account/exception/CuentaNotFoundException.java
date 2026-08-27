package com.backing.account.exception;

public class CuentaNotFoundException extends RuntimeException {

    public CuentaNotFoundException(Long id) {
        super("Cuenta con id " + id + " no encontrada");
    }
    

}
