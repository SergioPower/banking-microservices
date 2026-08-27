package com.backing.account.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backing.account.dto.CuentaRequest;
import com.backing.account.dto.CuentaResponse;
import com.backing.account.entity.Cuenta;
import com.backing.account.exception.CuentaInactivaException;
import com.backing.account.exception.CuentaNotFoundException;
import com.backing.account.exception.MontoInvalidoException;
import com.backing.account.exception.SaldoInsuficienteException;
import com.backing.account.repository.CuentaRepository;


@Service
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;

    public CuentaServiceImpl(CuentaRepository repository) {
        this.cuentaRepository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaResponse> findAll() {
        return cuentaRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Cuenta findById(Long id) {
        return cuentaRepository.findById(id).orElseThrow(() -> new CuentaNotFoundException());
    }
    
    @Override
    @Transactional(readOnly = true)
    public CuentaResponse findResponseById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Cuenta findByNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta).orElseThrow(() -> new CuentaNotFoundException());
    }
    @Override
    @Transactional
    public CuentaResponse crear(CuentaRequest request) {
        Cuenta nuevaCuenta = toEntity(request);
        Cuenta cuentaGuardada = cuentaRepository.save(nuevaCuenta);
        return toResponse(cuentaGuardada);

    }

    @Override
    @Transactional
    public CuentaResponse actualizar(Long id, CuentaRequest request) {
        Cuenta cuentaActualizada = findById(id);
        cuentaActualizada.setNumeroCuenta(request.numeroCuenta());
        cuentaActualizada.setTitular(request.titular());
        cuentaActualizada.setTipoCuenta(request.tipoCuenta());
        cuentaActualizada.setSaldo(request.saldoInicial());
        cuentaRepository.save(cuentaActualizada);
        return toResponse(cuentaActualizada);
    }

    @Override
    public void eliminar(Long id) {
        Cuenta eliminarCuenta = findById(id);
        cuentaRepository.delete(eliminarCuenta);
    }

    @Override
    public CuentaResponse depositar(Long id, BigDecimal monto) {
        Cuenta cuentaDepositar = findById(id);
        if (!cuentaDepositar.getActiva()) new CuentaInactivaException(cuentaDepositar.getNumeroCuenta());
        if (monto.compareTo(BigDecimal.ZERO) <= 0) new MontoInvalidoException();

        cuentaDepositar.setSaldo(cuentaDepositar.getSaldo().add(monto));
        cuentaRepository.save(cuentaDepositar);
        return toResponse(cuentaDepositar);
    }


    @Override
    public CuentaResponse retirar(Long id, BigDecimal monto) {
        Cuenta cuentaRetirar = findById(id);
        if (!cuentaRetirar.getActiva()) new CuentaInactivaException(cuentaRetirar.getNumeroCuenta());
        if (monto.compareTo(BigDecimal.ZERO) <= 0) new MontoInvalidoException();
        if (cuentaRetirar.getSaldo().compareTo(monto) == -1 ) new SaldoInsuficienteException(cuentaRetirar.getSaldo());

        cuentaRetirar.setSaldo(cuentaRetirar.getSaldo().subtract(monto));
        cuentaRepository.save(cuentaRetirar);
        return toResponse(cuentaRetirar);
    }

    public CuentaResponse toResponse(Cuenta cuenta){
        return new CuentaResponse(
            cuenta.getId(),
            cuenta.getNumeroCuenta(),
            cuenta.getTitular(),
            cuenta.getTipoCuenta(),
            cuenta.getSaldo(),
            cuenta.getActiva(),
            cuenta.getFechaCreacion()
        );
    }

    public Cuenta toEntity(CuentaRequest request){
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(request.numeroCuenta());
        cuenta.setTitular(request.titular());
        cuenta.setTipoCuenta(request.tipoCuenta());
        cuenta.setSaldo(request.saldoInicial());
        return cuenta;
    }


}
