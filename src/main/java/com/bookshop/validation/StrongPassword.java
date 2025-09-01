package com.bookshop.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface StrongPassword {
    String message() default "Password must be 12+ chars, include upper, lower, digit, and symbol";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


