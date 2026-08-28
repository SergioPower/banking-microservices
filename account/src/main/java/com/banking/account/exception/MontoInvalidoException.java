package com.banking.account.exception;

public class MontoInvalidoException extends RuntimeException{

    public MontoInvalidoException() {
        super("El monto debe ser mayor que 0");
    }
    

}
