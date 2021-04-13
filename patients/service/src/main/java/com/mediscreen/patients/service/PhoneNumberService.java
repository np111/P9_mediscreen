package com.mediscreen.patients.service;

import com.mediscreen.patients.api.model.PhoneCountries;

public interface PhoneNumberService {
    PhoneCountries getLocalizedPhoneCountries(String locale);

    String parsePhoneNumber(int countryCallingCode, String number);

    boolean isValidPhoneNumber(String str);
}
