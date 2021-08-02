package com.mediscreen.notes.mock;

import com.mediscreen.notes.api.model.Note;
import com.mediscreen.notes.persistence.entity.NoteEntity;
import com.mediscreen.notes.persistence.mapper.NoteMapper;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NoteMockData {
    private final NoteMapper noteMapper;

    public UUID getNoteIdA() {
        return UUID.fromString("d6501cf4-2fd2-448b-8dd6-3997dd3c1963");
    }

    public UUID getPatientIdA() {
        return UUID.fromString("b650a2bf-08e1-43ea-965d-f0b87ac21bfb");
    }

    public Note getNoteModelA() {
        return Note.builder()
                .id(getNoteIdA())
                .patientId(getPatientIdA())
                .createdAt(ZonedDateTime.of(2020, 1, 2, 3, 4, 5, 0, ZoneOffset.UTC))
                .updatedAt(ZonedDateTime.of(2020, 2, 3, 4, 5, 6, 0, ZoneOffset.UTC))
                .content("Lorem ipsum dolor sit amet, consectetur adipiscing elit.")
                .build();
    }

    public NoteEntity getNoteEntityA() {
        return noteMapper.fromModel(getNoteModelA());
    }

    public UUID getNoteIdB() {
        return UUID.fromString("6c8a639c-f509-486b-8e33-190719431894");
    }

    public UUID getPatientIdB() {
        return UUID.fromString("23f40323-d9b7-4b75-bad7-670ed1187d5a");
    }

    public Note getNoteModelB() {
        return Note.builder()
                .id(getNoteIdB())
                .patientId(getPatientIdB())
                .createdAt(ZonedDateTime.of(2021, 11, 12, 13, 14, 15, 0, ZoneOffset.UTC))
                .updatedAt(null)
                .content("Morbi laoreet fringilla dui vitae hendrerit.\nOrci varius natoque penatibus et magnis.")
                .build();
    }

    public NoteEntity getNoteEntityB() {
        return noteMapper.fromModel(getNoteModelB());
    }

    public List<Note> getNoteModels() {
        return new ArrayList<>(Arrays.asList(getNoteModelA(), getNoteModelB()));
    }

    public List<NoteEntity> getNoteEntities() {
        return new ArrayList<>(Arrays.asList(getNoteEntityA(), getNoteEntityB()));
    }
}
