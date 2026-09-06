package com.banking.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CuentaInactivaException.class)
    public ResponseEntity<String> handleCuentaInactiva(CuentaInactivaException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(CuentaNotFoundException.class)
    public ResponseEntity<String> handleCuentaNotFound(CuentaNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MontoInvalidoException.class)
    public ResponseEntity<String> handleMontoInvalido(MontoInvalidoException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(NumeroCuentaDuplicadoException.class)
    public ResponseEntity<String> handleNumeroCuentaDuplicado(NumeroCuentaDuplicadoException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<String> handleSaldoInsuficiente(SaldoInsuficienteException ex){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ex.getMessage());
    }

}
