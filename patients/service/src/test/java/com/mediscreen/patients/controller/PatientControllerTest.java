package com.mediscreen.patients.controller;

import com.mediscreen.patients.exception.PatientNotFoundException;
import com.mediscreen.patients.mock.PatientMockData;
import com.mediscreen.patients.mock.TestConfig;
import com.mediscreen.patients.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(PatientController.class)
@Import({TestConfig.class})
class PatientControllerTest extends AbstractControllerTest {
    @MockBean
    private PatientService patientService;

    @Autowired
    private PatientMockData patientMockData;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listPatients() throws Exception {
        doAnswer(m -> patientMockData.getPatientModels())
                .when(patientService).list();

        toMatchSnapshot(
                mockMvc.perform(get("/patient")).andReturn()
        );
    }

    @Test
    void getPatient() throws Exception {
        doAnswer(m -> patientMockData.getPatientModelA())
                .when(patientService).get(patientMockData.getPatientIdA());
        doThrow(new PatientNotFoundException(patientMockData.getPatientIdB()))
                .when(patientService).get(patientMockData.getPatientIdB());

        toMatchSnapshot(
                mockMvc.perform(get("/patient/" + patientMockData.getPatientIdA())).andReturn(),
                mockMvc.perform(get("/patient/" + patientMockData.getPatientIdB())).andReturn()
        );
    }

    @Test
    void createPatient() {
        // TBD
    }

    @Test
    void updatePatient() {
        // TBD
    }

    @Test
    void deletePatient() throws Exception {
        doAnswer(m -> patientMockData.getPatientModelA())
                .when(patientService).delete(patientMockData.getPatientIdA());
        doThrow(new PatientNotFoundException(patientMockData.getPatientIdB()))
                .when(patientService).delete(patientMockData.getPatientIdB());

        toMatchSnapshot(
                mockMvc.perform(delete("/patient/" + patientMockData.getPatientIdA())).andReturn(),
                mockMvc.perform(delete("/patient/" + patientMockData.getPatientIdB())).andReturn()
        );
    }
}