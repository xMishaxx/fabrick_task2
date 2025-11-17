package com.example.fabrick_task2.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IcaoCodeValidator implements ConstraintValidator<ValidIcaoCode, String> {

    private static final String ICAO_CODE_PATTERN = "^[A-Za-z]{4}$";

    @Override
    public void initialize(ValidIcaoCode constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return value.matches(ICAO_CODE_PATTERN);
    }
}