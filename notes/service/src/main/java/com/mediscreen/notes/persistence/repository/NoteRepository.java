package com.mediscreen.notes.persistence.repository;

import com.mediscreen.notes.persistence.entity.NoteEntity;
import java.util.UUID;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NoteRepository extends MongoRepository<NoteEntity, UUID> {
    boolean existsByPatientId(UUID patientId, TextCriteria textCriteria);
}
