package com.mediscreen.notes.controller;

import com.mediscreen.common.api.model.ApiError;
import com.mediscreen.common.api.model.ApiErrorType;
import com.mediscreen.common.api.validation.group.Create;
import com.mediscreen.common.api.validation.group.Update;
import com.mediscreen.common.spring.openapi.error.ApiErrorResponse;
import com.mediscreen.notes.api.model.Note;
import com.mediscreen.notes.api.model.NoteSearchTermRequest;
import com.mediscreen.notes.api.model.NoteSearchTermResult;
import com.mediscreen.notes.exception.NoteNotFoundException;
import com.mediscreen.notes.service.NoteService;
import java.util.UUID;
import javax.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/note")
@Validated
public class NoteController {
    private final NoteService noteService;

    @ApiErrorResponse(method = "handleNoteNotFoundException")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Note getNote(
            @PathVariable(name = "id") UUID id
    ) {
        return noteService.get(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Note createNote(
            @RequestBody @Validated({Default.class, Create.class}) Note note
    ) {
        return noteService.create(note);
    }

    @ApiErrorResponse(method = "handleNoteNotFoundException")
    @RequestMapping(method = RequestMethod.POST, value = "/{id}")
    public Note updateNote(
            @PathVariable(name = "id") UUID id,
            @RequestBody @Validated({Default.class, Update.class}) Note note
    ) {
        return noteService.update(id, note);
    }

    @ApiErrorResponse(method = "handleNoteNotFoundException")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteNote(
            @PathVariable(name = "id") UUID id
    ) {
        noteService.delete(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/searchTerm")
    public NoteSearchTermResult searchTerm(
            @RequestBody @Validated NoteSearchTermRequest req
    ) {
        return NoteSearchTermResult.builder()
                .matches(noteService.searchTerms(req.getPatientId(), req.getTerms()))
                .build();
    }

    @ExceptionHandler(NoteNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ApiError> handleNoteNotFoundException(NoteNotFoundException ex) {
        return new ResponseEntity<>(ApiError.builder()
                .type(ApiErrorType.SERVICE)
                .code("NOTE_NOT_FOUND")
                .message("Note does not exists")
                .metadata("id", ex.getId())
                .build(), HttpStatus.NOT_FOUND);
    }
}
