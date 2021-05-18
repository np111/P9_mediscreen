package com.mediscreen.assessment.service;

import java.time.ZonedDateTime;

public interface TimeService {
    /**
     * Returns the current time, to the second (so without nano-seconds).
     */
    ZonedDateTime now();
}
