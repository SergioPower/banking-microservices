package com.banking.transfer.exception;



public class TransferenciaNotFoundException extends RuntimeException{

	public TransferenciaNotFoundException(Long id) {
        super("La transferencia con id: " + id + "no fue encontrada");
	}
    

}
