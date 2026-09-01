package com.banking.transfer.exception;

public class CuentaOrigenDesnitoIgualException extends RuntimeException{

	public CuentaOrigenDesnitoIgualException() {
		super("La cuenta destino debe ser diferente a la cuenta origen");
	}
    
}
