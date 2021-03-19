package com.mediscreen.patients.service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.mediscreen.patients.api.model.Country;
import com.mediscreen.patients.api.model.PhoneCountries;
import com.mediscreen.patients.api.model.PhoneCountry;
import com.mediscreen.patients.exception.InvalidPhoneNumberException;
import com.mediscreen.patients.util.LocalizedMapUtil;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class PhoneNumberService implements InitializingBean {
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^[1-9][0-9]{0,3} [1-9][0-9]*$");

    private final PhoneNumberUtil pnu = PhoneNumberUtil.getInstance();
    private final CountryService countryService;

    private PhoneCountries phoneCountriesFallback;
    private Map<String, PhoneCountries> phoneCountriesByLocale;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, PhoneCountries> phoneCountriesByLocale = new HashMap<>();
        for (String locale : countryService.getSupportedLocales()) {
            phoneCountriesByLocale.put(locale, PhoneCountries.builder()
                    .locale(locale)
                    .countries(pnu.getSupportedRegions().stream()
                            .map(region -> PhoneCountry.builder()
                                    .regionCode(region)
                                    .callingCode(pnu.getCountryCodeForRegion(region))
                                    .name(getPhoneCountryName(locale, region))
                                    .build())
                            .sorted(Comparator.comparing(PhoneCountry::getName))
                            .collect(Collectors.toList()))
                    .build());
        }

        this.phoneCountriesFallback = Objects.requireNonNull(phoneCountriesByLocale.get("en"));
        this.phoneCountriesByLocale = Collections.unmodifiableMap(phoneCountriesByLocale);
    }

    private String getPhoneCountryName(String locale, String regionCode) {
        switch (regionCode) {
            case "SH":
                return "Saint Helena";
            case "AC":
                return "Ascension Island";
            case "TA":
                return "Tristan da Cunha";
            case "XK":
                return "Kosovo";
            default:
                Map<String, Country> countries = countryService.getLocalizedCountries(locale).getCountries();
                Country country = countries.get(regionCode.toLowerCase(Locale.ROOT));
                if (country == null) {
                    throw new IllegalArgumentException("Unknown phone country name: " + regionCode);
                }
                return country.getName();
        }
    }

    public PhoneCountries getLocalizedPhoneCountries(String locale) {
        return LocalizedMapUtil.get(phoneCountriesByLocale, locale, phoneCountriesFallback);
    }

    public String parsePhoneNumber(int countryCallingCode, String number) {
        String region = pnu.getRegionCodeForCountryCode(countryCallingCode);
        if ("ZZ".equals(region)) {
            throw new InvalidPhoneNumberException(countryCallingCode, number, "INVALID_COUNTRY_CODE", "Invalid country calling code.");
        }

        PhoneNumber phoneNumber;
        try {
            phoneNumber = pnu.parse(number, region);
        } catch (NumberParseException e) {
            throw new InvalidPhoneNumberException(countryCallingCode, number, e.getErrorType().name(), e.getMessage());
        }

        String ret = phoneNumber.getCountryCode() + " " + phoneNumber.getNationalNumber();
        if (!isValidPhoneNumber(ret)) {
            throw new InvalidPhoneNumberException(countryCallingCode, number, "NOT_A_NUMBER", "The string supplied did not seem to be a phone number.");
        }

        return ret;
    }

    public boolean isValidPhoneNumber(String str) {
        if (!PHONE_NUMBER_PATTERN.matcher(str).matches()) {
            return false;
        }
        int spaceIndex = str.indexOf(' ');
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setCountryCode(Integer.parseInt(str.substring(0, spaceIndex)));
        phoneNumber.setNationalNumber(Long.parseLong(str.substring(spaceIndex + 1)));
        return pnu.isValidNumber(phoneNumber);
    }
}
