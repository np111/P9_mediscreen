package com.mediscreen.notes.service;

import com.mediscreen.notes.api.model.Note;
import com.mediscreen.notes.exception.NoteNotFoundException;
import java.util.List;
import java.util.UUID;

public interface NoteService {
    List<Note> list();

    Note get(UUID id) throws NoteNotFoundException;

    Note create(Note note);

    Note update(UUID id, Note note) throws NoteNotFoundException;

    void delete(UUID id) throws NoteNotFoundException;

    boolean[] searchTerms(UUID patientId, List<String> terms);
}
