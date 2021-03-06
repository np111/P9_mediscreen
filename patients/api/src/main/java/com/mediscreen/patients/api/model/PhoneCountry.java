package com.mediscreen.patients.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@Builder(builderClassName = "Builder", toBuilder = true)
@Value
@With
@AllArgsConstructor(onConstructor = @__(@Deprecated)) // intended to be used only by tools (MapStruct, Jackson, etc)
public class PhoneCountry {
    String regionCode;

    /**
     * The country calling code (eg. 33 for France).
     */
    Integer callingCode;

    /**
     * The full country name.
     */
    String name;
}
