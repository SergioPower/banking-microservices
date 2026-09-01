package com.banking.transfer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CuentaOrigenDesnitoIgualException.class)
    public ResponseEntity<String> handleCuentaOrigenDestino(CuentaOrigenDesnitoIgualException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TransferenciaFallidaException.class)
    public ResponseEntity<String> handleTransferenciaFallida(TransferenciaFallidaException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(TransferenciaNotFoundException.class)
    public ResponseEntity<String> handleTransferenciaNotFound(TransferenciaNotFoundException ex){
        return ResponseEntity.status(404).body(ex.getMessage());
    }

}
