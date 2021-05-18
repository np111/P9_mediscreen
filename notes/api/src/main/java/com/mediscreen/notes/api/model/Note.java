package com.mediscreen.notes.api.model;

import com.mediscreen.common.api.validation.group.Update;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * A medical note about a patient.
 */
@Builder(builderClassName = "Builder", toBuilder = true)
@Value
@With
@AllArgsConstructor(onConstructor = @__(@Deprecated)) // intended to be used only by tools (MapStruct, Jackson, etc)
public class Note {
    UUID id;

    @NotNull(groups = Update.class)
    UUID patientId;

    ZonedDateTime createdAt;

    ZonedDateTime updatedAt;

    @NotNull(groups = Update.class)
    @Size(max = 10_000)
    String content;
}
