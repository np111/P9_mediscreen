package com.mediscreen.notes.service;

import com.mediscreen.notes.service.impl.TimeServiceImpl;
import java.time.Duration;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {
                TimeServiceImpl.class
        })
class TimeServiceTest {
    @Autowired
    private TimeService timeService;

    @Test
    void now() {
        ZonedDateTime excepted = ZonedDateTime.now();
        ZonedDateTime actual = timeService.now();
        assertEquals(0, actual.getNano());
        assertTrue(Duration.between(excepted, actual).abs().toMillis() < 5_000L);
    }
}