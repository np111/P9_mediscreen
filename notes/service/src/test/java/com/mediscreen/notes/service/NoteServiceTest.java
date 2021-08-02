package com.mediscreen.notes.service;

import com.mediscreen.notes.api.model.Note;
import com.mediscreen.notes.exception.NoteNotFoundException;
import com.mediscreen.notes.mock.NoteMockData;
import com.mediscreen.notes.persistence.mapper.NoteMapperImpl;
import com.mediscreen.notes.persistence.repository.NoteRepository;
import com.mediscreen.notes.service.impl.NoteServiceImpl;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.query.TextCriteria;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                NoteServiceImpl.class,
                NoteMapperImpl.class,
                NoteMockData.class
        })
class NoteServiceTest {
    @MockBean
    private TimeService timeService;

    @MockBean
    private NoteRepository noteRepository;

    @Autowired
    private NoteMockData noteMockData;

    @Autowired
    private NoteService noteService;

    @BeforeEach
    void setup() {
        ZonedDateTime now = ZonedDateTime.now().withNano(0);
        doAnswer(m -> now).when(timeService).now();
    }

    @Test
    void list() {
        doAnswer(m -> noteMockData.getNoteEntities())
                .when(noteRepository).findAll();
        doAnswer(m -> Collections.singletonList(noteMockData.getNoteEntityA()))
                .when(noteRepository).findAllByPatientId(noteMockData.getPatientIdA());

        assertEquals(noteMockData.getNoteModels(), noteService.list(null));
        assertEquals(Collections.singletonList(noteMockData.getNoteModelA()), noteService.list(noteMockData.getPatientIdA()));
        assertEquals(Collections.emptyList(), noteService.list(noteMockData.getPatientIdB()));
    }

    @Test
    void get() {
        doAnswer(m -> Optional.of(noteMockData.getNoteEntityA()))
                .when(noteRepository).findById(noteMockData.getNoteIdA());

        assertEquals(noteMockData.getNoteModelA(), noteService.get(noteMockData.getNoteIdA()));

        assertThrows(NoteNotFoundException.class, () -> noteService.get(noteMockData.getNoteIdB()));
    }

    @Test
    void create() {
        doAnswer(m -> m.getArgument(0))
                .when(noteRepository).save(any());

        Note note = noteService.create(noteMockData.getNoteModelA());
        assertNotEquals(noteMockData.getNoteIdA(), note.getId());
        assertEquals(
                noteMockData.getNoteModelA()
                        .withCreatedAt(timeService.now())
                        .withUpdatedAt(timeService.now()),
                note.withId(noteMockData.getNoteIdA()));
    }

    @Test
    void update() {
        doAnswer(m -> Optional.of(noteMockData.getNoteEntityA()))
                .when(noteRepository).findById(noteMockData.getNoteIdA());
        doAnswer(m -> m.getArgument(0))
                .when(noteRepository).save(any());

        assertEquals(
                noteMockData.getNoteModelB()
                        .withId(noteMockData.getNoteIdA())
                        .withCreatedAt(noteMockData.getNoteModelA().getCreatedAt())
                        .withUpdatedAt(timeService.now()),
                noteService.update(noteMockData.getNoteIdA(), noteMockData.getNoteModelB()));

        assertThrows(NoteNotFoundException.class,
                () -> noteService.update(noteMockData.getNoteIdB(), noteMockData.getNoteModelB()));
    }

    @Test
    void delete() {
        doAnswer(m -> Optional.of(noteMockData.getNoteEntityA()))
                .when(noteRepository).findById(noteMockData.getNoteIdA());

        noteService.delete(noteMockData.getNoteIdA());
        verify(noteRepository, times(1)).deleteById(noteMockData.getNoteIdA());

        assertThrows(NoteNotFoundException.class, () -> noteService.delete(noteMockData.getNoteIdB()));
    }

    @Test
    void searchTerms() {
        doAnswer(m -> {
            TextCriteria criteria = m.getArgument(1);
            String search = criteria.getCriteriaObject().get("$text", Document.class).getString("$search");
            return "b".equals(search) || "\"d e\"".equals(search);
        }).when(noteRepository).existsByPatientId(eq(noteMockData.getPatientIdA()), any());

        boolean[] res = noteService.searchTerms(noteMockData.getPatientIdA(), Arrays.asList("a", "b", "c", "d e"));
        assertArrayEquals(new boolean[]{false, true, false, true}, res);
    }
}