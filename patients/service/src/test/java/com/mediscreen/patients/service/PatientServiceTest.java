package com.mediscreen.patients.service;

import com.mediscreen.patients.api.model.Patient;
import com.mediscreen.patients.exception.PatientNotFoundException;
import com.mediscreen.patients.mock.PatientMockData;
import com.mediscreen.patients.mock.TestConfig;
import com.mediscreen.patients.persistence.mapper.PatientMapperImpl;
import com.mediscreen.patients.persistence.repository.PatientRepository;
import com.mediscreen.patients.service.impl.PatientServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(
        webEnvironment = WebEnvironment.NONE,
        classes = {
                PatientServiceImpl.class,
                PatientMapperImpl.class
        })
@Import({TestConfig.class})
class PatientServiceTest {
    @MockBean
    private PatientRepository patientRepository;

    @Autowired
    private PatientMockData patientMockData;

    @Autowired
    private PatientService patientService;

    @Test
    void list() {
        doAnswer(m -> patientMockData.getPatientEntities())
                .when(patientRepository).findAll();

        assertEquals(patientMockData.getPatientModels(), patientService.list());
    }

    @Test
    void get() {
        doAnswer(m -> Optional.of(patientMockData.getPatientEntityA()))
                .when(patientRepository).findById(patientMockData.getPatientIdA());

        assertEquals(patientMockData.getPatientModelA(), patientService.get(patientMockData.getPatientIdA()));

        assertThrows(PatientNotFoundException.class, () -> patientService.get(patientMockData.getPatientIdB()));
    }

    @Test
    void create() {
        doAnswer(m -> m.getArgument(0))
                .when(patientRepository).save(any());

        Patient patient = patientService.create(patientMockData.getPatientModelA());
        assertNotEquals(patientMockData.getPatientIdA(), patient.getId());
        assertEquals(patientMockData.getPatientModelA(), patient.withId(patientMockData.getPatientIdA()));
    }

    @Test
    void update() {
        doAnswer(m -> Optional.of(patientMockData.getPatientEntityA()))
                .when(patientRepository).findById(patientMockData.getPatientIdA());
        doAnswer(m -> m.getArgument(0))
                .when(patientRepository).save(any());

        assertEquals(patientMockData.getPatientModelB().withId(patientMockData.getPatientIdA()),
                patientService.update(patientMockData.getPatientIdA(), patientMockData.getPatientModelB()));

        assertThrows(PatientNotFoundException.class,
                () -> patientService.update(patientMockData.getPatientIdB(), patientMockData.getPatientModelB()));
    }

    @Test
    void delete() {
        doAnswer(m -> Optional.of(patientMockData.getPatientEntityA()))
                .when(patientRepository).findById(patientMockData.getPatientIdA());

        patientService.delete(patientMockData.getPatientIdA());
        verify(patientRepository, times(1)).deleteById(patientMockData.getPatientIdA());

        assertThrows(PatientNotFoundException.class, () -> patientService.delete(patientMockData.getPatientIdB()));
    }
}