package com.backing.account.service;

import java.util.List;

import com.backing.account.dto.CuentaRequest;
import com.backing.account.dto.CuentaResponse;
import com.backing.account.dto.MontoRequest;
import com.backing.account.entity.Cuenta;

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
