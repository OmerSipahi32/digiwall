package com.inghubs.digiwall.model.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IbanValidator implements ConstraintValidator<ValidIban, String> {

    private static final String IBAN_REGEX = "^[A-Z]{2}\\d{2}[A-Z0-9]{1,30}$";

    @Override
    public void initialize(ValidIban constraintAnnotation) {
    }

    @Override
    public boolean isValid(String iban, ConstraintValidatorContext context) {
        if (iban == null || iban.isBlank()) return false;
        return iban.matches(IBAN_REGEX);
    }
}