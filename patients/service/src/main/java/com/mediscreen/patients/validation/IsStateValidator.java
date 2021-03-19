package com.mediscreen.patients.validation;

import com.mediscreen.common.spring.validation.SpringConstraintValidator;
import com.mediscreen.common.spring.validation.ValidatorHelper;
import com.mediscreen.patients.api.validation.constraint.IsState;
import javax.validation.ConstraintValidatorContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class IsStateValidator extends SpringConstraintValidator<IsState, String> implements IsState.Validator {
    private static final int MAX_LENGTH = 255;

    private boolean strict;

    @Override
    public void doInitialize(IsState annotation) {
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
