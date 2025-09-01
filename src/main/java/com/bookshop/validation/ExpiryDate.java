package com.bookshop.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ExpiryDateValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface ExpiryDate {
    String message() default "Invalid expiry date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


