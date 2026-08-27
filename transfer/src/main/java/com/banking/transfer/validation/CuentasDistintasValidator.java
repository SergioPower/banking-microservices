package com.banking.transfer.validation;

import com.banking.transfer.dto.TransferenciaRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CuentasDistintasValidator implements ConstraintValidator<CuentasDistintas, TransferenciaRequest> {

    @Override
    public boolean isValid(TransferenciaRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        Long origenId = request.cuentaOrigenId();
        Long destinoId = request.cuentaDestinoId();

        if (origenId == null || destinoId == null) {
            return true;
        }

        return !origenId.equals(destinoId);
    }
}