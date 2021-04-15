package com.mediscreen.assessment.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.assessment.properties.PatientsServiceProperties;
import com.mediscreen.assessment.service.PatientsClient;
import com.mediscreen.common.restclient.JacksonRestSerializer;
import com.mediscreen.common.restclient.OkHttpRestClient;
import com.mediscreen.common.restclient.RestCall;
import com.mediscreen.common.restclient.RestClient;
import com.mediscreen.patients.api.model.Patient;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HttpPatientsClient implements PatientsClient {
    private final RestClient restClient;

    @Autowired
    public HttpPatientsClient(ObjectMapper objectMapper, PatientsServiceProperties props) {
        restClient = OkHttpRestClient.builder()
                .baseUrl(props.getBaseUrl())
                .maxRequests(props.getMaxConcurrentRequests())
                .serializer(new JacksonRestSerializer(objectMapper))
                .build();
    }

    @Override
    public CompletableFuture<Optional<Patient>> getPatient(UUID patientId) {
        return restClient.call(RestCall.of(Patient.class).path("patient").path(patientId.toString()))
                .thenCompose(r -> {
                    if (r.isSuccess()) {
                        return CompletableFuture.completedFuture(Optional.of(r.getResult()));
                    }
                    if ("PATIENT_NOT_FOUND".equals(r.getError().getCode())) {
                        return CompletableFuture.completedFuture(Optional.empty());
                    }
                    return CompletableFuture.failedFuture(new RuntimeException("Unhandled ApiError: " + r.getError().getCode()));
                });
    }
}
