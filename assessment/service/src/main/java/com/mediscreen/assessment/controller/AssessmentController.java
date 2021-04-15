package com.mediscreen.assessment.controller;

import com.mediscreen.assessment.api.model.CheckRiskRequest;
import com.mediscreen.assessment.api.model.CheckRiskResult;
import com.mediscreen.assessment.exception.IncompletePatientException;
import com.mediscreen.assessment.exception.PatientNotFoundException;
import com.mediscreen.assessment.service.AssessmentService;
import com.mediscreen.common.api.model.ApiError;
import com.mediscreen.common.api.model.ApiErrorType;
import com.mediscreen.common.spring.openapi.error.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/assessment")
@Validated
public class AssessmentController {
    private final AssessmentService assessmentService;

    @ApiErrorResponse(method = "handlePatientNotFoundException")
    @ApiErrorResponse(method = "handleIncompletePatientException")
    @RequestMapping(method = RequestMethod.POST)
    public CheckRiskResult checkRisk(
            @RequestBody @Validated CheckRiskRequest req
    ) {
        return assessmentService.checkRiskLevel(req.getPatientId());
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

    @ExceptionHandler(IncompletePatientException.class)
    @ResponseBody
    public ResponseEntity<ApiError> handleIncompletePatientException(IncompletePatientException ex) {
        return new ResponseEntity<>(ApiError.builder()
                .type(ApiErrorType.SERVICE)
                .code("INCOMPLETE_PATIENT")
                .message("Patient is incomplete (birthdate or gender is missing)")
                .metadata("field", ex.getField())
                .build(), HttpStatus.NOT_FOUND);
    }
}
