package com.mediscreen.patients.service;

import com.mediscreen.patients.api.model.Patient;
import com.mediscreen.patients.exception.PatientNotFoundException;
import java.util.List;
import java.util.UUID;

public interface PatientService {
    List<Patient> list();

    Patient get(UUID id) throws PatientNotFoundException;

    Patient create(Patient patient);

    Patient update(UUID id, Patient patient) throws PatientNotFoundException;

    void delete(UUID id) throws PatientNotFoundException;
}
