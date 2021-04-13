package com.mediscreen.assessment.exception;

import com.mediscreen.common.util.exception.FastRuntimeException;
import lombok.Getter;

@Getter
public class IncompletePatientException extends FastRuntimeException {
    private final String field;

    public IncompletePatientException(String field) {
        super("Patient is incomplete: missing " + field);
        this.field = field;
    }
}
