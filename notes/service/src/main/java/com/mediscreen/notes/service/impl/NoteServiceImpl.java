package com.mediscreen.notes.service.impl;

import com.mediscreen.common.util.UuidUtil;
import com.mediscreen.notes.api.model.Note;
import com.mediscreen.notes.exception.NoteNotFoundException;
import com.mediscreen.notes.persistence.entity.NoteEntity;
import com.mediscreen.notes.persistence.mapper.NoteMapper;
import com.mediscreen.notes.persistence.repository.NoteRepository;
import com.mediscreen.notes.service.NoteService;
import com.mediscreen.notes.service.TimeService;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class NoteServiceImpl implements NoteService {
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final TimeService timeService;

    @Override
    public List<Note> list(UUID patientId) {
        List<NoteEntity> notes = patientId == null ? noteRepository.findAll() : noteRepository.findAllByPatientId(patientId);
        return notes.stream().map(noteMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public Note get(UUID id) throws NoteNotFoundException {
        NoteEntity entity = noteRepository.findById(id).orElse(null);
        if (entity == null) {
            throw new NoteNotFoundException(id);
        }
        return noteMapper.toModel(entity);
    }

    @Override
    public Note create(Note note) {
        NoteEntity entity = noteMapper.fromModel(note);
        entity.setId(UuidUtil.sequentialUUID(ThreadLocalRandom.current()));
        entity.setCreatedAt(timeService.now());
        entity.setUpdatedAt(entity.getCreatedAt());
        return noteMapper.toModel(noteRepository.save(entity));
    }

    @Override
    public Note update(UUID id, Note note) throws NoteNotFoundException {
        NoteEntity entity = noteRepository.findById(id).orElse(null);
        if (entity == null) {
            throw new NoteNotFoundException(id);
        }
        ZonedDateTime originalCreatedAt = entity.getCreatedAt();
        noteMapper.fromModel(entity, note);
        entity.setId(id);
        entity.setCreatedAt(originalCreatedAt);
        entity.setUpdatedAt(timeService.now());
        return noteMapper.toModel(noteRepository.save(entity));
    }

    @Override
    public void delete(UUID id) throws NoteNotFoundException {
        NoteEntity entity = noteRepository.findById(id).orElse(null);
        if (entity == null) {
            throw new NoteNotFoundException(id);
        }
        noteRepository.deleteById(id);
    }

    @Override
    public boolean[] searchTerms(UUID patientId, List<String> terms) {
        boolean[] ret = new boolean[terms.size()];
        for (int i = 0; i < terms.size(); i++) {
            String term = terms.get(i);
            if (term.contains(" ")) {
                term = "\"" + term + "\"";
            }
            ret[i] = noteRepository.existsByPatientId(patientId, new TextCriteria().matching(term));
        }
        return ret;
    }
}
