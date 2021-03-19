package com.mediscreen.patients.validation;

import com.mediscreen.common.spring.validation.SpringConstraintValidator;
import com.mediscreen.patients.api.validation.constraint.IsCountryCode;
import com.mediscreen.patients.service.CountryService;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
@Scope("prototype")
public class IsCountryCodeValidator extends SpringConstraintValidator<IsCountryCode, String> implements IsCountryCode.Validator {
    private final CountryService countryService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        return value == null || countryService.isCountryCode(value);
    }
}
