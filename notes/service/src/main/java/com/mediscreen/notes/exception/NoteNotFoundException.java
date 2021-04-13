package com.mediscreen.notes.exception;

import com.mediscreen.common.util.exception.FastRuntimeException;
import java.util.UUID;
import lombok.Getter;

@Getter
public class NoteNotFoundException extends FastRuntimeException {
    private final UUID id;

    public NoteNotFoundException(UUID id) {
        super("Note not found: " + id);
        this.id = id;
    }
}
