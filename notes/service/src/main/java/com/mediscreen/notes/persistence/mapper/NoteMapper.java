package com.mediscreen.notes.persistence.mapper;

import com.mediscreen.common.spring.config.MapperConfig;
import com.mediscreen.notes.api.model.Note;
import com.mediscreen.notes.persistence.entity.NoteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface NoteMapper {
    Note toModel(NoteEntity entity);

    NoteEntity fromModel(Note model);

    void fromModel(@MappingTarget NoteEntity target, Note model);
}
