package com.mediscreen.patients.service;

import com.mediscreen.patients.api.model.PhoneCountries;
import com.mediscreen.patients.exception.InvalidPhoneNumberException;

/**
 * Phone number service.
 */
public interface PhoneNumberService {
    /**
     * Returns the list of phones countries in the given locale.
     *
     * @param locale The language in which the country names should be translated (falling-back to english).
     */
    PhoneCountries getLocalizedPhoneCountries(String locale);

    /**
     * Parse a phone number entered by a user.
     *
     * @param countryCallingCode The country chosen by the user
     * @param number             The user input
     * @return The valid (parsed) phone number
     * @throws InvalidPhoneNumberException if the input is not a parsable number
     */
    String parsePhoneNumber(int countryCallingCode, String number) throws InvalidPhoneNumberException;

    /**
     * Checks if a value is a valid (parsed) phone number.
     */
    boolean isValidPhoneNumber(String str);
}
