package com.banking.account.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking.account.dto.CuentaRequest;
import com.banking.account.dto.CuentaResponse;
import com.banking.account.dto.MontoRequest;
import com.banking.account.entity.Cuenta;
import com.banking.account.exception.CuentaInactivaException;
import com.banking.account.exception.CuentaNotFoundException;
import com.banking.account.exception.MontoInvalidoException;
import com.banking.account.exception.NumeroCuentaDuplicadoException;
import com.banking.account.exception.SaldoInsuficienteException;
import com.banking.account.repository.CuentaRepository;

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
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException("La cuenta con id: " + id + "no fue encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaResponse findResponseById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Cuenta findByNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new CuentaNotFoundException("No se encontró la cuenta con número: " + numeroCuenta));
    }

    @Override
    @Transactional
    public CuentaResponse crear(CuentaRequest request) {
        if (cuentaRepository.existsByNumeroCuenta(request.numeroCuenta())) {
            throw new NumeroCuentaDuplicadoException(request.numeroCuenta());
        }

        Cuenta nuevaCuenta = toEntity(request);
        Cuenta cuentaGuardada = cuentaRepository.save(nuevaCuenta);
        return toResponse(cuentaGuardada);

    }

    @Override
    @Transactional
    public CuentaResponse actualizar(Long id, CuentaRequest request) {
        Cuenta cuentaActualizada = findById(id);
        if (!cuentaActualizada.getNumeroCuenta().equals(request.numeroCuenta()) &&
                cuentaRepository.existsByNumeroCuenta(request.numeroCuenta())) {
            throw new NumeroCuentaDuplicadoException(request.numeroCuenta());
        }

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
    public CuentaResponse depositar(Long id, MontoRequest montoRequest) {
        Cuenta cuentaDepositar = findById(id);
        BigDecimal monto = montoRequest.monto();
        if (cuentaRepository.existsByNumeroCuenta(cuentaDepositar.getNumeroCuenta()) && 
            !cuentaDepositar.getActiva())
            throw new CuentaInactivaException(cuentaDepositar.getNumeroCuenta());
        if (monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new MontoInvalidoException();

        cuentaDepositar.setSaldo(cuentaDepositar.getSaldo().add(monto));
        cuentaRepository.save(cuentaDepositar);
        return toResponse(cuentaDepositar);
    }

    @Override
    public CuentaResponse retirar(Long id, MontoRequest montoRequest) {
        Cuenta cuentaRetirar = findById(id);
        BigDecimal monto = montoRequest.monto();

        if (cuentaRepository.existsByNumeroCuenta(cuentaRetirar.getNumeroCuenta()) && !cuentaRetirar.getActiva())
            throw new CuentaInactivaException(cuentaRetirar.getNumeroCuenta());
        if (monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new MontoInvalidoException();
        if (cuentaRetirar.getSaldo().compareTo(monto) == -1)
            throw new SaldoInsuficienteException(cuentaRetirar.getSaldo());

        cuentaRetirar.setSaldo(cuentaRetirar.getSaldo().subtract(monto));
        cuentaRepository.save(cuentaRetirar);
        return toResponse(cuentaRetirar);
    }

    public CuentaResponse toResponse(Cuenta cuenta) {
        return new CuentaResponse(
                cuenta.getId(),
                cuenta.getNumeroCuenta(),
                cuenta.getTitular(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldo(),
                cuenta.getActiva(),
                cuenta.getFechaCreacion());
    }

    public Cuenta toEntity(CuentaRequest request) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(request.numeroCuenta());
        cuenta.setTitular(request.titular());
        cuenta.setTipoCuenta(request.tipoCuenta());
        cuenta.setSaldo(request.saldoInicial());
        return cuenta;
    }

}
