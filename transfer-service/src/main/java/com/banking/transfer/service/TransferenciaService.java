package com.banking.transfer.service;

import java.util.List;

import com.banking.transfer.dto.TransferenciaRequest;
import com.banking.transfer.dto.TransferenciaResponse;
import com.banking.transfer.entity.Transferencia;

public interface TransferenciaService {
    List<TransferenciaResponse> findAll();

    Transferencia findById(Long id);

    TransferenciaResponse findResponseById(Long id);

    TransferenciaResponse crearTransferencia(TransferenciaRequest request);

    TransferenciaResponse toResponse(Transferencia transferencia);

}
