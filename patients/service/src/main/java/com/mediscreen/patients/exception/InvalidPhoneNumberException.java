package com.mediscreen.patients.exception;

import com.mediscreen.common.util.exception.FastRuntimeException;
import lombok.Getter;

@Getter
public class InvalidPhoneNumberException extends FastRuntimeException {
    private final int countryCallingCode;
    private final String number;
    private final String parseError;

    public InvalidPhoneNumberException(int countryCallingCode, String number, String parseError, String message) {
        super(message);
        this.countryCallingCode = countryCallingCode;
        this.number = number;
        this.parseError = parseError;
    }
}
