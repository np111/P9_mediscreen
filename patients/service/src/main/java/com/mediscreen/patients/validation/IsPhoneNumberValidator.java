package com.mediscreen.patients.validation;

import com.mediscreen.common.spring.validation.SpringConstraintValidator;
import com.mediscreen.patients.api.validation.constraint.IsPhoneNumber;
import com.mediscreen.patients.service.PhoneNumberService;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
@Scope("prototype")
public class IsPhoneNumberValidator extends SpringConstraintValidator<IsPhoneNumber, String> implements IsPhoneNumber.Validator {
    private final PhoneNumberService phoneNumberService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        return value == null || phoneNumberService.isValidPhoneNumber(value);
    }
}
