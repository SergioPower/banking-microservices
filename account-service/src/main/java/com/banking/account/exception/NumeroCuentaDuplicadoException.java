package com.banking.account.exception;

public class NumeroCuentaDuplicadoException extends RuntimeException {

    public NumeroCuentaDuplicadoException(String numeroCuenta) {
        super("El número de cuenta " + numeroCuenta + " ya existe");
    }

}
