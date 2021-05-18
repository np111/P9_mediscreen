package com.mediscreen.patients.controller;

import com.mediscreen.common.api.model.ApiError;
import com.mediscreen.common.api.model.ApiErrorType;
import com.mediscreen.common.api.validation.group.Create;
import com.mediscreen.common.api.validation.group.Update;
import com.mediscreen.common.spring.openapi.error.ApiErrorResponse;
import com.mediscreen.patients.api.model.Patient;
import com.mediscreen.patients.exception.PatientNotFoundException;
import com.mediscreen.patients.service.PatientService;
import java.util.List;
import java.util.UUID;
import javax.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/patient")
@Validated
public class PatientController {
    private final PatientService patientService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Patient> listPatients() {
        return patientService.list();
    }

    @ApiErrorResponse(method = "handlePatientNotFoundException")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Patient getPatient(
            @PathVariable(name = "id") UUID id
    ) {
        return patientService.get(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Patient createPatient(
            @RequestBody @Validated({Default.class, Create.class}) Patient patient
    ) {
        return patientService.create(patient);
    }

    @ApiErrorResponse(method = "handlePatientNotFoundException")
    @RequestMapping(method = RequestMethod.POST, value = "/{id}")
    public Patient updatePatient(
            @PathVariable(name = "id") UUID id,
            @RequestBody @Validated({Default.class, Update.class}) Patient patient
    ) {
        return patientService.update(id, patient);
    }

    @ApiErrorResponse(method = "handlePatientNotFoundException")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deletePatient(
            @PathVariable(name = "id") UUID id
    ) {
        patientService.delete(id);
    }

    @ExceptionHandler(PatientNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ApiError> handlePatientNotFoundException(PatientNotFoundException ex) {
        return new ResponseEntity<>(ApiError.builder()
                .type(ApiErrorType.SERVICE)
                .code("PATIENT_NOT_FOUND")
                .message("Patient does not exists")
                .metadata("id", ex.getId())
                .build(), HttpStatus.NOT_FOUND);
    }
}
