package com.example.fabrick_task2.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IcaoCodeValidator.class)
@Documented
public @interface ValidIcaoCode {

    String message() default "ICAO code must be exactly 4 alphabetic characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}