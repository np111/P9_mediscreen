package com.mediscreen.assessment.exception;

import com.mediscreen.common.util.exception.FastRuntimeException;
import java.util.UUID;
import lombok.Getter;

@Getter
public class PatientNotFoundException extends FastRuntimeException {
    private final UUID id;

    public PatientNotFoundException(UUID id) {
        super("Patient not found: " + id);
        this.id = id;
    }
}
