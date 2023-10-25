package com.eric.appointment.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrentPasswordMatchValidator.class)
public @interface CurrentPasswordMatch {
    
    String message() default "Wrong Current Password";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
