package com.banking.transfer.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CuentasDistintasValidator.class)
@Documented
public @interface CuentasDistintas {

    String message() default "La cuenta origen y la cuenta destino deben ser diferentes";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}