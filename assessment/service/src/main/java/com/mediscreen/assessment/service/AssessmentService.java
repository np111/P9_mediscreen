package com.mediscreen.assessment.service;

import com.mediscreen.assessment.api.model.CheckRiskResult;
import com.mediscreen.assessment.exception.IncompletePatientException;
import com.mediscreen.assessment.exception.PatientNotFoundException;
import java.util.UUID;

public interface AssessmentService {
    CheckRiskResult checkRiskLevel(UUID patientId) throws PatientNotFoundException, IncompletePatientException;
}
