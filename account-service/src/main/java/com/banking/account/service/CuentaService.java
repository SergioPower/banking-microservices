package com.banking.account.service;

import java.util.List;

import com.banking.account.dto.CuentaRequest;
import com.banking.account.dto.CuentaResponse;
import com.banking.account.dto.MontoRequest;
import com.banking.account.entity.Cuenta;

public interface CuentaService {
    List<CuentaResponse> findAll();

    Cuenta findById(Long id);

    CuentaResponse findResponseById(Long id);

    Cuenta findByNumeroCuenta(String numeroCuenta);

    CuentaResponse crear(CuentaRequest request);

    CuentaResponse actualizar(Long id, CuentaRequest request);

    void eliminar(Long id);

    CuentaResponse depositar(Long id, MontoRequest montoRequest);

    CuentaResponse retirar(Long id, MontoRequest montoRequest);

}
