package com.mediscreen.assessment.service;

import com.mediscreen.patients.api.model.Patient;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PatientsClient {
    CompletableFuture<Optional<Patient>> getPatient(UUID patientId);
}
