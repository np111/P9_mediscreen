package com.mediscreen.common.spring.validation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidatorContext;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidatorHelper {
    public static boolean failWithDetails(ConstraintValidatorContext ctx, String details) {
        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate(ctx.getDefaultConstraintMessageTemplate() + " (" + details + ")").addConstraintViolation();
        return false;
    }

    public static boolean checkMaxLength(ConstraintValidatorContext ctx, int value, int maxLength) {
        if (value > maxLength) {
            return ValidatorHelper.failWithDetails(ctx, "too long - max length: " + maxLength);
        }
        return true;
    }

    public static boolean checkPattern(ConstraintValidatorContext ctx, String value, Pattern pattern) {
        if (!pattern.matcher(value).matches()) {
            return ValidatorHelper.failWithDetails(ctx, "bad pattern: " + pattern.pattern());
        }
        return true;
    }
}
