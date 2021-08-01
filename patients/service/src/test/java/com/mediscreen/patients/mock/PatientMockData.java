package com.mediscreen.patients.mock;

import com.mediscreen.patients.api.model.Patient;
import com.mediscreen.patients.persistence.entity.PatientEntity;
import com.mediscreen.patients.persistence.mapper.PatientMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PatientMockData {
    private final PatientMapper patientMapper;

    public UUID getPatientIdA() {
        return UUID.fromString("b650a2bf-08e1-43ea-965d-f0b87ac21bfb");
    }

    public Patient getPatientModelA() {
        return Patient.builder()
                .id(getPatientIdA())
                .firstName("John")
                .lastName("Smith")
                .build();
    }

    public PatientEntity getPatientEntityA() {
        return patientMapper.fromModel(getPatientModelA());
    }

    public UUID getPatientIdB() {
        return UUID.fromString("23f40323-d9b7-4b75-bad7-670ed1187d5a");
    }

    public Patient getPatientModelB() {
        return Patient.builder()
                .id(getPatientIdB())
                .firstName("Mary")
                .lastName("Johnson")
                .build();
    }

    public PatientEntity getPatientEntityB() {
        return patientMapper.fromModel(getPatientModelB());
    }

    public List<Patient> getPatientModels() {
        return new ArrayList<>(Arrays.asList(getPatientModelA(), getPatientModelB()));
    }

    public List<PatientEntity> getPatientEntities() {
        return new ArrayList<>(Arrays.asList(getPatientEntityA(), getPatientEntityB()));
    }
}
