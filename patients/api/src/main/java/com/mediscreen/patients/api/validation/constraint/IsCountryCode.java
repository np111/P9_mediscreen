package com.mediscreen.patients.api.validation.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.Payload;

/**
 * The annotated {@link String} must be a lower-case ISO 3166-1 alpha-2 country code.
 * <p>
 * {@code null} elements are considered valid.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
        ElementType.PARAMETER, ElementType.TYPE_USE})
@Constraint(validatedBy = IsCountryCode.Validator.class)
@Documented
public @interface IsCountryCode {
    String message() default "must be a country code";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    interface Validator extends ConstraintValidator<IsCountryCode, String> {
    }
}
