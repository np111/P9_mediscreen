package com.mediscreen.assessment.service;

import com.mediscreen.notes.api.model.NoteSearchTermRequest;
import com.mediscreen.notes.api.model.NoteSearchTermResult;
import java.util.concurrent.CompletableFuture;

public interface NotesClient {
    CompletableFuture<NoteSearchTermResult> searchTerm(NoteSearchTermRequest request);
}
