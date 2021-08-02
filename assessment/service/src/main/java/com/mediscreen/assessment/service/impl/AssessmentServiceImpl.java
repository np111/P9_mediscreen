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
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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
                .terms(RiskTerm.getAllPhrases())
                .build()).join();
        Set<RiskTerm> matchingTerms = EnumSet.noneOf(RiskTerm.class);
        boolean[] matches = res.getMatches();
        for (int i = 0; i < matches.length; i++) {
            if (matches[i]) {
                matchingTerms.add(RiskTerm.byPhraseIndex(i));
            }
        }
        int matchingTermsCount = matchingTerms.size();

        return CheckRiskResult.builder()
                .riskLevel(determineRiskLevel(patient, matchingTermsCount))
                .terms(matchingTerms.stream().map(t -> t.getPhrases()[0]).collect(Collectors.toList()))
                .build();
    }

    public RiskLevel determineRiskLevel(Patient patient, int termsCount) {
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

    @Getter
    private enum RiskTerm {
        HEMOGLOBIN_A1C("hemoglobin a1c", "hemoglobine a1c"),
        MICROALBUMIN("microalbumin", "microalbumine"),
        HEIGHT("height", "taille"),
        WEIGHT("weight", "poids"),
        SMOKER("smoker", "fumeur"),
        ABNORMAL("abnormal", "anormal"),
        CHOLESTEROL("cholesterol"),
        RELAPSE("relapse", "rechute"),
        DIZZINESS("dizziness", "vertige"),
        REACTION("reaction"),
        ANTIBODIES("antibodies", "anticorps"),
        ;

        private static final List<String> ALL_PHRASES;
        private static final List<RiskTerm> BY_PHRASE_INDEX;

        static {
            List<String> allPhrases = new ArrayList<>();
            List<RiskTerm> byPhraseIndex = new ArrayList<>();
            for (RiskTerm riskTerm : values()) {
                for (String term : riskTerm.getPhrases()) {
                    allPhrases.add(term);
                    byPhraseIndex.add(riskTerm);
                }
            }
            ALL_PHRASES = Collections.unmodifiableList(allPhrases);
            BY_PHRASE_INDEX = Collections.unmodifiableList(byPhraseIndex);
        }

        public static List<String> getAllPhrases() {
            return ALL_PHRASES;
        }

        public static RiskTerm byPhraseIndex(int i) {
            return BY_PHRASE_INDEX.get(i);
        }

        private final String[] phrases;

        RiskTerm(String... phrases) {
            this.phrases = phrases;
        }
    }
}
