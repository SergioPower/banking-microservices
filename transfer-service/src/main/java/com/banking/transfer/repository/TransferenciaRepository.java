package com.banking.transfer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banking.transfer.entity.Transferencia;

public interface TransferenciaRepository extends JpaRepository<Transferencia, Long>{
    List<Transferencia> findByCuentaOrigenId(Long cuentaOrigenId);

    List<Transferencia> findByCuentaDestinoId(Long cuentaDestinoId);
}
