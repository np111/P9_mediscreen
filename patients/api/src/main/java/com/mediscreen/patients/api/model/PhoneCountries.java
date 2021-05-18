package com.mediscreen.patients.api.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.With;

/**
 * List of phone countries.
 */
@Builder(builderClassName = "Builder", toBuilder = true)
@Value
@With
@AllArgsConstructor(onConstructor = @__(@Deprecated)) // intended to be used only by tools (MapStruct, Jackson, etc)
public class PhoneCountries {
    /**
     * The locale in which the {@linkplain PhoneCountry#getName() country names} are translated.
     */
    String locale;

    /**
     * The countries.
     */
    @Singular("country")
    List<PhoneCountry> countries;
}
