package com.mediscreen.patients.service;

import com.mediscreen.patients.api.model.Patient;
import com.mediscreen.patients.exception.PatientNotFoundException;
import java.util.List;
import java.util.UUID;

/**
 * Patients management service.
 */
public interface PatientService {
    /**
     * Returns a list of all patients.
     */
    List<Patient> list();

    /**
     * Returns the patient with the given ID.
     *
     * @throws PatientNotFoundException if the patient does not exist
     */
    Patient get(UUID id) throws PatientNotFoundException;

    /**
     * Create a new patient.
     *
     * @return The created patient (with ID completed).
     */
    Patient create(Patient patient);

    /**
     * Update an existing patient with the given ID.
     *
     * @return The updated patient.
     * @throws PatientNotFoundException if the patient does not exist
     */
    Patient update(UUID id, Patient patient) throws PatientNotFoundException;

    /**
     * Delete the patient with the given ID.
     *
     * @throws PatientNotFoundException if the patient does not exist
     */
    void delete(UUID id) throws PatientNotFoundException;
}
