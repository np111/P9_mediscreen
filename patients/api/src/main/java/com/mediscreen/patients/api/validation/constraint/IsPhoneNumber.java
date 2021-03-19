package com.mediscreen.patients.api.validation.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
        ElementType.PARAMETER, ElementType.TYPE_USE})
@Constraint(validatedBy = IsPhoneNumber.Validator.class)
@Documented
public @interface IsPhoneNumber {
    String message() default "must be a phone number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    interface Validator extends ConstraintValidator<IsPhoneNumber, String> {
    }
}
