package com.mediscreen.notes.persistence.entity;

import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notes")
@NoArgsConstructor
@Data
public class NoteEntity {
    @Id
    private UUID id;
    private UUID patientId;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private String content;
}
