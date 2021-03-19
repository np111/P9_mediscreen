package com.mediscreen.patients.validation;

import com.mediscreen.common.spring.validation.SpringConstraintValidator;
import com.mediscreen.common.spring.validation.ValidatorHelper;
import com.mediscreen.patients.api.validation.constraint.IsPostalCode;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidatorContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class IsPostalCodeValidator extends SpringConstraintValidator<IsPostalCode, String> implements IsPostalCode.Validator {
    private static final int MAX_LENGTH = 16;
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        if (value == null) {
            return true;
        }

        int len = value.length();
        if (!ValidatorHelper.checkMaxLength(ctx, len, MAX_LENGTH)) {
            return false;
        }

        if (!ValidatorHelper.checkPattern(ctx, value, PATTERN)) {
            return false;
        }

        return true;
    }
}
