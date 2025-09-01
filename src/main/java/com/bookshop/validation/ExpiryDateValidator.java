package com.bookshop.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.YearMonth;

public class ExpiryDateValidator implements ConstraintValidator<ExpiryDate, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        if (!value.matches("^(0[1-9]|1[0-2])/[0-9]{2}$")) return false;
        int month = Integer.parseInt(value.substring(0, 2));
        int year = Integer.parseInt(value.substring(3, 5));
        int currentCentury = YearMonth.now().getYear() / 100 * 100;
        YearMonth expiry = YearMonth.of(currentCentury + year, month);
        YearMonth now = YearMonth.now();
        return !expiry.isBefore(now);
    }
}


