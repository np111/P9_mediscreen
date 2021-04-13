package com.mediscreen.assessment.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.assessment.service.NotesClient;
import com.mediscreen.common.restclient.JacksonRestSerializer;
import com.mediscreen.common.restclient.OkHttpRestClient;
import com.mediscreen.common.restclient.RestCall;
import com.mediscreen.common.restclient.RestClient;
import com.mediscreen.notes.api.model.NoteSearchTermRequest;
import com.mediscreen.notes.api.model.NoteSearchTermResult;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HttpNotesClient implements NotesClient {
    private final RestClient restClient;

    @Autowired
    public HttpNotesClient(ObjectMapper objectMapper) {
        restClient = OkHttpRestClient.builder()
                .baseUrl("http://localhost:8082/") // TODO: Properties
                .serializer(new JacksonRestSerializer(objectMapper))
                .build();
    }

    @Override
    public CompletableFuture<NoteSearchTermResult> searchTerm(NoteSearchTermRequest request) {
        return restClient.call(RestCall.of(NoteSearchTermResult.class).path("note").path("searchTerm").post(request))
                .thenCompose(r -> {
                    if (r.isSuccess()) {
                        return CompletableFuture.completedFuture(r.getResult());
                    }
                    return CompletableFuture.failedFuture(new RuntimeException("Unhandled ApiError: " + r.getError().getCode()));
                });
    }
}
