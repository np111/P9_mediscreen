package com.mediscreen.patients.service;

import com.mediscreen.patients.exception.InvalidPhoneNumberException;
import com.mediscreen.patients.mock.TestConfig;
import com.mediscreen.patients.service.impl.CountryServiceImpl;
import com.mediscreen.patients.service.impl.PhoneNumberServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = WebEnvironment.NONE,
        classes = {
                PhoneNumberServiceImpl.class,
                CountryServiceImpl.class
        })
@Import({TestConfig.class})
class PhoneNumberServiceTest {
    @Autowired
    private PhoneNumberService phoneService;

    @Test
    void getLocalizedPhoneCountries() {
        // test non-fallback
        assertNotEquals(phoneService.getLocalizedPhoneCountries("en"), phoneService.getLocalizedPhoneCountries("fr"));

        // test fallback
        assertEquals(phoneService.getLocalizedPhoneCountries("en"), phoneService.getLocalizedPhoneCountries("zz"));
        assertEquals(phoneService.getLocalizedPhoneCountries("en"), phoneService.getLocalizedPhoneCountries("INVALID"));
    }

    @Test
    void parsePhoneNumber() {
        assertEquals("1 2025550156", phoneService.parsePhoneNumber(1, "202-555-0156"));
        assertEquals("1 2025550156", phoneService.parsePhoneNumber(33, "+1-202-555-0156"));
        assertEquals("33 612345678", phoneService.parsePhoneNumber(33, "612345678"));
        assertEquals("33 612345678", phoneService.parsePhoneNumber(33, "6 12 34 56 78"));
        assertEquals("33 612345678", phoneService.parsePhoneNumber(33, "6 1234 5678"));

        assertThrows(InvalidPhoneNumberException.class,
                () -> phoneService.parsePhoneNumber(9999, "202-555-0156"),
                "Should throws InvalidPhoneNumberException");

        assertThrows(InvalidPhoneNumberException.class,
                () -> phoneService.parsePhoneNumber(1, "100-100-1000"),
                "Should throws InvalidPhoneNumberException");
    }

    @Test
    void isValidPhoneNumber() {
        assertTrue(phoneService.isValidPhoneNumber("1 2025550156"));
        assertTrue(phoneService.isValidPhoneNumber("33 612345678"));

        assertFalse(phoneService.isValidPhoneNumber("33"));
        assertFalse(phoneService.isValidPhoneNumber("612345678"));
        assertFalse(phoneService.isValidPhoneNumber("1 1001001000"));
    }
}