package com.banking.transfer.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.banking.transfer.client.CuentaClient;
import com.banking.transfer.dto.CuentaResponse;
import com.banking.transfer.dto.TransferenciaRequest;
import com.banking.transfer.dto.TransferenciaResponse;
import com.banking.transfer.entity.EstadoTransferencia;
import com.banking.transfer.entity.Transferencia;
import com.banking.transfer.exception.TransferenciaNotFoundException;
import com.banking.transfer.repository.TransferenciaRepository;

@Service
public class TransferenciaServiceImpl implements TransferenciaService{

    private final TransferenciaRepository repository;

    private final CuentaClient cuentaClient;

    public TransferenciaServiceImpl(TransferenciaRepository repository, CuentaClient cuentaClient) {
        this.repository = repository;
        this.cuentaClient = cuentaClient;
    }

    @Override
    public TransferenciaResponse crearTransferencia(TransferenciaRequest request) {
        // 1. validar cuenta origen != destino
        if (request.cuentaOrigenId().equals(request.cuentaDestinoId())) {
            throw new RuntimeException();
        }

        /* 2. consultar cuenta origen
                ↓
        account-service */
        CuentaResponse cuentaOrigen = cuentaClient.obtenerCuenta(request.cuentaOrigenId());

        /* 3. consultar cuenta destino
            ↓
        account-service */
        CuentaResponse cuentaDestino = cuentaClient.obtenerCuenta(request.cuentaDestinoId());

        // 4. validar saldo
        BigDecimal monto = request.monto();
        if (cuentaOrigen.saldo().compareTo(monto) == -1 ) {
            throw new RuntimeException();
        }

        // 5. retirar dinero de origen
        cuentaClient.retirar(cuentaOrigen.id(), monto);

        // 6. depositar dinero en destino
        cuentaClient.depositar(cuentaDestino.id(), monto);

        //7. guardar transferencia como COMPLETADA
        Transferencia transferencia = new Transferencia();
        transferencia.setCuentaOrigenId(cuentaOrigen.id());
        transferencia.setCuentaDestinoId(cuentaDestino.id());
        transferencia.setMonto(monto);
        transferencia.setEstado(EstadoTransferencia.COMPLETADA);
        repository.save(transferencia);

        return toResponse(transferencia);
    }

    

    @Override
    public List<TransferenciaResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public Transferencia findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new TransferenciaNotFoundException());
    }

    @Override
    public TransferenciaResponse findResponseById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    public TransferenciaResponse toResponse(Transferencia transferencia) {
            return new TransferenciaResponse(
                transferencia.getId(),
                transferencia.getCuentaOrigenId(),
                transferencia.getCuentaDestinoId(),
                transferencia.getMonto(),
                transferencia.getEstado(),
                transferencia.getFechaCreacion(),
                transferencia.getReferencia()
            );
    }


}
