package com.mediscreen.patients.service;

import com.mediscreen.patients.api.model.Countries;
import com.mediscreen.patients.api.model.Country;
import java.util.Collection;

/**
 * Country management service (ISO 3166-1).
 */
public interface CountryService {
    /**
     * Returns the list of supported languages for {@linkplain Country#getName() country names}.
     */
    Collection<String> getSupportedLocales();

    /**
     * Checks if a value is a valid country code.
     */
    boolean isCountryCode(String value);

    /**
     * Returns the list of countries in the given locale.
     *
     * @param locale The language in which the country names should be translated (falling-back to english).
     */
    Countries getLocalizedCountries(String locale);
}
