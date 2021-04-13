package com.mediscreen.notes.service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Service;

@Service
public class TimeService {
    public ZonedDateTime now() {
        return ZonedDateTime.now(ZoneOffset.UTC).withNano(0);
    }
}
