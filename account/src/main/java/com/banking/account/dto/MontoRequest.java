package com.banking.account.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MontoRequest(

        @NotNull @Positive BigDecimal monto
) {
    @JsonCreator
    public MontoRequest {
    }

}
