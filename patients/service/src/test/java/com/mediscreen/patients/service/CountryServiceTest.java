package com.mediscreen.patients.service;

import com.mediscreen.patients.mock.TestConfig;
import com.mediscreen.patients.service.impl.CountryServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = WebEnvironment.NONE,
        classes = {
                CountryServiceImpl.class
        })
@Import({TestConfig.class})
class CountryServiceTest {
    @Autowired
    private CountryService countryService;

    @Test
    void getSupportedLocales() {
        assertFalse(countryService.getSupportedLocales().isEmpty());
    }

    @Test
    void isCountryCode() {
        assertTrue(countryService.isCountryCode("us"));
        assertTrue(countryService.isCountryCode("fr"));

        assertFalse(countryService.isCountryCode("en"));
        assertFalse(countryService.isCountryCode("fr-FR"));
        assertFalse(countryService.isCountryCode("zz"));
    }

    @Test
    void getLocalizedCountries() {
        // test non-fallback
        assertNotEquals(countryService.getLocalizedCountries("en"), countryService.getLocalizedCountries("fr"));

        // test fallback
        assertEquals(countryService.getLocalizedCountries("en"), countryService.getLocalizedCountries("zz"));
        assertEquals(countryService.getLocalizedCountries("en"), countryService.getLocalizedCountries("INVALID"));
    }
}