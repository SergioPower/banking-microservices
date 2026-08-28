package com.banking.transfer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.transfer.dto.TransferenciaRequest;
import com.banking.transfer.dto.TransferenciaResponse;
import com.banking.transfer.service.TransferenciaServiceImpl;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaController {

    private final TransferenciaServiceImpl transferenciaServiceImpl;

    public TransferenciaController(TransferenciaServiceImpl transferenciaServiceImpl) {
        this.transferenciaServiceImpl = transferenciaServiceImpl;
    }

    @GetMapping
    public ResponseEntity<List<TransferenciaResponse>> getFindAll() {
        return ResponseEntity.ok(transferenciaServiceImpl.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferenciaResponse> getFindById(@PathVariable Long id) {
        return ResponseEntity.ok(transferenciaServiceImpl.findResponseById(id));
    }
    
    @PostMapping
    public ResponseEntity<TransferenciaResponse> postCrearTransferencia(@RequestBody @Valid TransferenciaRequest request) {
        return ResponseEntity.status(201).body(transferenciaServiceImpl.crearTransferencia(request));
    }

}
