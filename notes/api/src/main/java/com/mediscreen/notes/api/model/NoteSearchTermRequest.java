package com.mediscreen.notes.api.model;

import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@Builder(builderClassName = "Builder", toBuilder = true)
@Value
@With
@AllArgsConstructor(onConstructor = @__(@Deprecated)) // intended to be used only by tools (MapStruct, Jackson, etc)
public class NoteSearchTermRequest {
    @NotNull
    UUID patientId;

    @NotEmpty
    List<@NotNull String> terms;
}
