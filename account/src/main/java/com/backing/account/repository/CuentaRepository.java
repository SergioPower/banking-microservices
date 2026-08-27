package com.backing.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backing.account.entity.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

    boolean existByNumeroCuenta(String numeroCuenta);

}