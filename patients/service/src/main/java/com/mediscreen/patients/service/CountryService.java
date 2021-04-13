package com.mediscreen.patients.service;

import com.mediscreen.patients.api.model.Countries;
import java.util.Collection;

public interface CountryService {
    Collection<String> getSupportedLocales();

    boolean isCountryCode(String value);

    Countries getLocalizedCountries(String locale);
}
