package com.mediscreen.patients.validation;

import com.mediscreen.common.spring.validation.SpringConstraintValidator;
import com.mediscreen.common.spring.validation.ValidatorHelper;
import com.mediscreen.patients.api.validation.constraint.IsAddress;
import javax.validation.ConstraintValidatorContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class IsAddressValidator extends SpringConstraintValidator<IsAddress, String> implements IsAddress.Validator {
    private static final int MAX_LENGTH = 255;

    private boolean strict;

    @Override
    public void doInitialize(IsAddress annotation) {
        strict = annotation.strict();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        if (value == null) {
            return true;
        }

        int len = value.length();
        if (!ValidatorHelper.checkMaxLength(ctx, len, MAX_LENGTH)) {
            return false;
        }

        if (strict) {
            // TBD: Strictly validate content
        }

        return true;
    }
}
