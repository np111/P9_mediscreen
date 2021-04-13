package com.mediscreen.assessment.service.impl;

import com.mediscreen.assessment.api.model.CheckRiskResult;
import com.mediscreen.assessment.api.model.RiskLevel;
import com.mediscreen.assessment.exception.IncompletePatientException;
import com.mediscreen.assessment.exception.PatientNotFoundException;
import com.mediscreen.assessment.service.AssessmentService;
import com.mediscreen.assessment.service.NotesClient;
import com.mediscreen.assessment.service.PatientsClient;
import com.mediscreen.assessment.service.TimeService;
import com.mediscreen.notes.api.model.NoteSearchTermRequest;
import com.mediscreen.notes.api.model.NoteSearchTermResult;
import com.mediscreen.patients.api.model.Gender;
import com.mediscreen.patients.api.model.Patient;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class AssessmentServiceImpl implements AssessmentService {
    private final TimeService timeService;
    private final PatientsClient patientsClient;
    private final NotesClient notesClient;

    @Override
    public CheckRiskResult checkRiskLevel(UUID patientId) throws PatientNotFoundException, IncompletePatientException {
        Patient patient = patientsClient.getPatient(patientId).join().orElse(null);
        if (patient == null) {
            throw new PatientNotFoundException(patientId);
        }
        if (patient.getBirthdate() == null) {
            throw new IncompletePatientException("birthdate");
        }
        if (patient.getGender() == null) {
            throw new IncompletePatientException("gender");
        }

        NoteSearchTermResult res = notesClient.searchTerm(NoteSearchTermRequest.builder()
                .patientId(patientId)
                .terms(Arrays.stream(RiskTerm.values()).map(r -> "\"" + r.getTerm() + "\"").collect(Collectors.toList()))
                .build()).join();
        List<String> terms = IntStream.range(0, RiskTerm.values().length)
                .mapToObj(i -> res.getMatches()[i] ? RiskTerm.values()[i].getTerm() : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        int termsCount = terms.size();

        return CheckRiskResult.builder()
                .riskLevel(determineRiskLevel(patient, termsCount))
                .terms(terms)
                .build();
    }

    private RiskLevel determineRiskLevel(Patient patient, int termsCount) {
        int age = Period.between(patient.getBirthdate(), timeService.now().toLocalDate()).getYears();
        if (age < 30) {
            boolean male = patient.getGender() == Gender.MALE;
            if (termsCount >= (male ? 5 : 7)) {
                return RiskLevel.EARLY_ONSET;
            }
            if (termsCount >= (male ? 3 : 4)) {
                return RiskLevel.IN_DANGER;
            }
        } else {
            if (termsCount >= 8) {
                return RiskLevel.EARLY_ONSET;
            }
            if (termsCount >= 6) {
                return RiskLevel.IN_DANGER;
            }
            if (termsCount >= 2) {
                return RiskLevel.BORDERLINE;
            }
        }
        return RiskLevel.NONE;
    }

    @RequiredArgsConstructor
    @Getter
    private enum RiskTerm {
        HEMOGLOBIN_A1C("hemoglobin a1c"),
        MICROALBUMIN("microalbumin"),
        HEIGHT("height"),
        WEIGHT("weight"),
        SMOKER("smoker"),
        ABNORMAL("abnormal"),
        CHOLESTEROL("cholesterol"),
        DIZZINESS("dizziness"),
        REACTION("reaction"),
        ANTIBODIES("antibodies"),
        ;

        private final String term;
    }
}
