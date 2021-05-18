package com.mediscreen.notes.service;

import com.mediscreen.notes.api.model.Note;
import com.mediscreen.notes.exception.NoteNotFoundException;
import java.util.List;
import java.util.UUID;

/**
 * Notes management service.
 */
public interface NoteService {
    /**
     * Returns a list of all notes of a patient.
     *
     * @param patientId The concerned patient; or {@code null} for all
     */
    List<Note> list(UUID patientId);

    /**
     * Returns the note with the given ID.
     *
     * @throws NoteNotFoundException if the note does not exist
     */
    Note get(UUID id) throws NoteNotFoundException;

    /**
     * Create a new note.
     *
     * @return The created note (with all fields completed).
     */
    Note create(Note note);

    /**
     * Update an existing note with the given ID.
     *
     * @return The updated note.
     * @throws NoteNotFoundException if the note does not exist
     */
    Note update(UUID id, Note note) throws NoteNotFoundException;

    /**
     * Delete the note with the given ID.
     *
     * @throws NoteNotFoundException if the note does not exist
     */
    void delete(UUID id) throws NoteNotFoundException;

    /**
     * Search for specific terms in a patient's notes.
     *
     * @param patientId Patient ID
     * @param terms     List of terms to search
     * @return A list of booleans, which indicate whether each term was found or not (in order).
     */
    boolean[] searchTerms(UUID patientId, List<String> terms);
}
