package com.mediscreen.assessment.service;

import com.mediscreen.assessment.api.model.CheckRiskResult;
import com.mediscreen.assessment.api.model.RiskLevel;
import com.mediscreen.assessment.exception.IncompletePatientException;
import com.mediscreen.assessment.exception.PatientNotFoundException;
import com.mediscreen.assessment.service.impl.AssessmentServiceImpl;
import com.mediscreen.notes.api.model.NoteSearchTermRequest;
import com.mediscreen.notes.api.model.NoteSearchTermResult;
import com.mediscreen.patients.api.model.Gender;
import com.mediscreen.patients.api.model.Patient;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                AssessmentServiceImpl.class,
        })
class AssessmentServiceTest {
    @MockBean
    private TimeService timeService;

    @MockBean
    private NotesClient notesClient;

    @MockBean
    private PatientsClient patientsClient;

    @Autowired
    private AssessmentServiceImpl assessmentService;

    @BeforeEach
    void setup() {
        ZonedDateTime now = ZonedDateTime.now().withNano(0);
        doAnswer(m -> now).when(timeService).now();
    }

    @Test
    void checkRiskLevel() {
        UUID patientNone = UUID.randomUUID();
        UUID patientEmpty = UUID.randomUUID();
        UUID patientIncomplete1 = UUID.randomUUID();
        UUID patientIncomplete2 = UUID.randomUUID();
        UUID patientFull = UUID.randomUUID();
        int age = 30;
        Gender gender = Gender.MALE;
        doAnswer(m -> {
            UUID patientId = m.getArgument(0);
            Patient patient;
            if (patientEmpty.equals(patientId)) {
                patient = getPatient(null, null);
            } else if (patientIncomplete1.equals(patientId)) {
                patient = getPatient(age, null);
            } else if (patientIncomplete2.equals(patientId)) {
                patient = getPatient(null, gender);
            } else if (patientFull.equals(patientId)) {
                patient = getPatient(age, gender);
            } else {
                patient = null;
            }
            return CompletableFuture.completedFuture(Optional.ofNullable(patient));
        }).when(patientsClient).getPatient(any());

        List<String> matchingTerms = Arrays.asList("hemoglobin a1c", "microalbumin");
        doAnswer(m -> {
            NoteSearchTermRequest req = m.getArgument(0);
            boolean[] matches = new boolean[req.getTerms().size()];
            for (int i = 0; i < req.getTerms().size(); ++i) {
                matches[i] = matchingTerms.contains(req.getTerms().get(i));
            }
            return CompletableFuture.completedFuture(NoteSearchTermResult.builder().matches(matches).build());
        }).when(notesClient).searchTerm(any());

        assertThrows(PatientNotFoundException.class, () -> assessmentService.checkRiskLevel(patientNone));
        assertThrows(IncompletePatientException.class, () -> assessmentService.checkRiskLevel(patientEmpty));
        assertThrows(IncompletePatientException.class, () -> assessmentService.checkRiskLevel(patientIncomplete1));
        assertThrows(IncompletePatientException.class, () -> assessmentService.checkRiskLevel(patientIncomplete2));

        CheckRiskResult res = assessmentService.checkRiskLevel(patientFull);
        assertEquals(CheckRiskResult.builder().riskLevel(RiskLevel.BORDERLINE).terms(matchingTerms).build(), res);
    }

    @Test
    void determineRiskLevel() {
        Patient male29 = getPatient(29, Gender.MALE);
        assertEquals(RiskLevel.NONE, assessmentService.determineRiskLevel(male29, 0));
        assertEquals(RiskLevel.NONE, assessmentService.determineRiskLevel(male29, 1));
        assertEquals(RiskLevel.NONE, assessmentService.determineRiskLevel(male29, 2));
        assertEquals(RiskLevel.IN_DANGER, assessmentService.determineRiskLevel(male29, 3));
        assertEquals(RiskLevel.IN_DANGER, assessmentService.determineRiskLevel(male29, 4));
        assertEquals(RiskLevel.EARLY_ONSET, assessmentService.determineRiskLevel(male29, 5));
        assertEquals(RiskLevel.EARLY_ONSET, assessmentService.determineRiskLevel(male29, 6));
        assertEquals(RiskLevel.EARLY_ONSET, assessmentService.determineRiskLevel(male29, 7));
        assertEquals(RiskLevel.EARLY_ONSET, assessmentService.determineRiskLevel(male29, 8));
        assertEquals(RiskLevel.EARLY_ONSET, assessmentService.determineRiskLevel(male29, 9));

        Patient male30 = getPatient(30, Gender.MALE);
        assertEquals(RiskLevel.NONE, assessmentService.determineRiskLevel(male30, 0));
        assertEquals(RiskLevel.NONE, assessmentService.determineRiskLevel(male30, 1));
        assertEquals(RiskLevel.BORDERLINE, assessmentService.determineRiskLevel(male30, 2));
        assertEquals(RiskLevel.BORDERLINE, assessmentService.determineRiskLevel(male30, 3));
        assertEquals(RiskLevel.BORDERLINE, assessmentService.determineRiskLevel(male30, 4));
        assertEquals(RiskLevel.BORDERLINE, assessmentService.determineRiskLevel(male30, 5));
        assertEquals(RiskLevel.IN_DANGER, assessmentService.determineRiskLevel(male30, 6));
        assertEquals(RiskLevel.IN_DANGER, assessmentService.determineRiskLevel(male30, 7));
        assertEquals(RiskLevel.EARLY_ONSET, assessmentService.determineRiskLevel(male30, 8));
        assertEquals(RiskLevel.EARLY_ONSET, assessmentService.determineRiskLevel(male30, 9));

        Patient female29 = getPatient(29, Gender.FEMALE);
        assertEquals(RiskLevel.NONE, assessmentService.determineRiskLevel(female29, 0));
        assertEquals(RiskLevel.NONE, assessmentService.determineRiskLevel(female29, 1));
        assertEquals(RiskLevel.NONE, assessmentService.determineRiskLevel(female29, 2));
        assertEquals(RiskLevel.NONE, assessmentService.determineRiskLevel(female29, 3));
        assertEquals(RiskLevel.IN_DANGER, assessmentService.determineRiskLevel(female29, 4));
        assertEquals(RiskLevel.IN_DANGER, assessmentService.determineRiskLevel(female29, 5));
        assertEquals(RiskLevel.IN_DANGER, assessmentService.determineRiskLevel(female29, 6));
        assertEquals(RiskLevel.EARLY_ONSET, assessmentService.determineRiskLevel(female29, 7));
        assertEquals(RiskLevel.EARLY_ONSET, assessmentService.determineRiskLevel(female29, 8));
        assertEquals(RiskLevel.EARLY_ONSET, assessmentService.determineRiskLevel(female29, 9));

        Patient female30 = getPatient(30, Gender.FEMALE);
        assertEquals(RiskLevel.NONE, assessmentService.determineRiskLevel(female30, 0));
        assertEquals(RiskLevel.NONE, assessmentService.determineRiskLevel(female30, 1));
        assertEquals(RiskLevel.BORDERLINE, assessmentService.determineRiskLevel(female30, 2));
        assertEquals(RiskLevel.BORDERLINE, assessmentService.determineRiskLevel(female30, 3));
        assertEquals(RiskLevel.BORDERLINE, assessmentService.determineRiskLevel(female30, 4));
        assertEquals(RiskLevel.BORDERLINE, assessmentService.determineRiskLevel(female30, 5));
        assertEquals(RiskLevel.IN_DANGER, assessmentService.determineRiskLevel(female30, 6));
        assertEquals(RiskLevel.IN_DANGER, assessmentService.determineRiskLevel(female30, 7));
        assertEquals(RiskLevel.EARLY_ONSET, assessmentService.determineRiskLevel(female30, 8));
        assertEquals(RiskLevel.EARLY_ONSET, assessmentService.determineRiskLevel(female30, 9));
    }

    private Patient getPatient(Integer age, Gender gender) {
        return Patient.builder()
                .birthdate(age == null ? null : timeService.now().toLocalDate().minusYears(age))
                .gender(gender)
                .build();
    }
}