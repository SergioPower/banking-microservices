package com.backing.account.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backing.account.dto.CuentaRequest;
import com.backing.account.dto.CuentaResponse;
import com.backing.account.dto.MontoRequest;
import com.backing.account.service.CuentaService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping
    public ResponseEntity<List<CuentaResponse>> getFindAll() {
        return ResponseEntity.ok(cuentaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponse> getFindResponseById(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.findResponseById(id));
    }

    @PostMapping
    public ResponseEntity<CuentaResponse> postCrear(@RequestBody @Valid CuentaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cuentaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaResponse> putActualizar(@PathVariable Long id, @RequestBody CuentaRequest request) {
        return ResponseEntity.ok(cuentaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CuentaResponse> deleteEliminar(@PathVariable Long id) {
        cuentaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // OPERACIONES BANCARIAS

    @PostMapping("/{id}/depositos")
    public ResponseEntity<CuentaResponse> postDepositar(@PathVariable Long id, @RequestBody MontoRequest montoRequest) {
        return ResponseEntity.ok(cuentaService.depositar(id, montoRequest));
    }

    @PostMapping("/{id}/retiros")
    public ResponseEntity<CuentaResponse> postRetirar(@PathVariable Long id, @RequestBody MontoRequest montoRequest) {
        return ResponseEntity.ok(cuentaService.retirar(id, montoRequest));
    }

}
