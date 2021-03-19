package com.mediscreen.patients.controller;

import com.mediscreen.common.api.model.ApiError;
import com.mediscreen.common.api.model.ApiErrorType;
import com.mediscreen.common.spring.openapi.error.ApiErrorResponse;
import com.mediscreen.patients.api.model.ParsePhoneNumberRequest;
import com.mediscreen.patients.api.model.PhoneCountries;
import com.mediscreen.patients.exception.InvalidPhoneNumberException;
import com.mediscreen.patients.service.PhoneNumberService;
import javax.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/phone")
@Validated
public class PhoneNumberController {
    private final PhoneNumberService phoneNumberService;

    @RequestMapping(method = RequestMethod.GET, value = "/country")
    public PhoneCountries listPhoneCountries(
            @RequestParam(name = "locale", required = false) @Size(max = 255) String locale
    ) {
        return phoneNumberService.getLocalizedPhoneCountries(locale);
    }

    @ApiErrorResponse(
            description = "Unable to parse and validate the given phone number",
            method = "handlePhoneNumberParseException"
    )
    @RequestMapping(method = RequestMethod.POST, value = "/parse")
    public String parsePhoneNumber(
            @Validated @RequestBody ParsePhoneNumberRequest req
    ) {
        return phoneNumberService.parsePhoneNumber(req.getCountryCallingCode(), req.getNumber());
    }

    @ExceptionHandler(InvalidPhoneNumberException.class)
    @ResponseBody
    public ResponseEntity<ApiError> handlePhoneNumberParseException(InvalidPhoneNumberException ex) {
        return new ResponseEntity<>(ApiError.builder()
                .type(ApiErrorType.SERVICE)
                .code("INVALID_PHONE_NUMBER")
                .message(ex.getMessage())
                .metadata("countryCallingCode", ex.getCountryCallingCode())
                .metadata("number", ex.getNumber())
                .metadata("parseError", ex.getParseError())
                .build(), HttpStatus.NOT_FOUND);
    }
}
