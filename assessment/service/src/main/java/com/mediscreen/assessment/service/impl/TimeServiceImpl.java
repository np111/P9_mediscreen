package com.mediscreen.assessment.service.impl;

import com.mediscreen.assessment.service.TimeService;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Service;

@Service
public class TimeServiceImpl implements TimeService {
    @Override
    public ZonedDateTime now() {
        return ZonedDateTime.now(ZoneOffset.UTC).withNano(0);
    }
}
