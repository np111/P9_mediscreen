package com.mediscreen.patients.api.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.With;

/**
 * List of countries.
 */
@Builder(builderClassName = "Builder", toBuilder = true)
@Value
@With
@AllArgsConstructor(onConstructor = @__(@Deprecated)) // intended to be used only by tools (MapStruct, Jackson, etc)
public class Countries {
    /**
     * The locale in which the {@linkplain Country#getName() country names} are translated.
     */
    String locale;

    /**
     * The countries, associated with their {@linkplain Country#getCode() code}.
     */
    @Singular("country")
    Map<String, Country> countries;
}
