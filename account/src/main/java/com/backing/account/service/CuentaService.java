package com.backing.account.service;

import java.math.BigDecimal;
import java.util.List;

import com.backing.account.dto.CuentaRequest;
import com.backing.account.dto.CuentaResponse;
import com.backing.account.entity.Cuenta;

public interface CuentaService {
    List<CuentaResponse> findAll();

    Cuenta findById(Long id);

    CuentaResponse findResponseById(Long id);

    Cuenta findByNumeroCuenta(String numeroCuenta);

    CuentaResponse crear(CuentaRequest request);

    CuentaResponse actualizar(Long id, CuentaRequest request);

    void eliminar(Long id);

    CuentaResponse depositar(Long id, BigDecimal monto);

    CuentaResponse retirar(Long id, BigDecimal monto);

}
