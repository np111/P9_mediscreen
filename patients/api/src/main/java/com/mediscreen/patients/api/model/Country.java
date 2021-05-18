package com.mediscreen.patients.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@Builder(builderClassName = "Builder", toBuilder = true)
@Value
@With
@AllArgsConstructor(onConstructor = @__(@Deprecated)) // intended to be used only by tools (MapStruct, Jackson, etc)
public class Country {
    /**
     * The lower-case ISO 3166-1 alpha-2 country code.
     */
    String code;

    /**
     * The full country name.
     */
    String name;
}
