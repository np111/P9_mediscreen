package com.mediscreen.assessment.service;

import com.mediscreen.assessment.api.model.CheckRiskResult;
import com.mediscreen.assessment.exception.IncompletePatientException;
import com.mediscreen.assessment.exception.PatientNotFoundException;
import java.util.UUID;

public interface AssessmentService {
    /**
     * Checks the risk level of a patient.
     *
     * @param patientId Patient ID
     * @return the assessed level of risk
     * @throws PatientNotFoundException   if the patient does not exist
     * @throws IncompletePatientException if the patient's birthdate or gender is not defined
     */
    CheckRiskResult checkRiskLevel(UUID patientId) throws PatientNotFoundException, IncompletePatientException;
}
