package com.mediscreen.patients.util;

import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LocalizedMapUtilTest {
    private final Map<String, Integer> map = Map.of(
            "fr", 1,
            "fr-CA", 2,
            "en-US", 3
    );

    @Test
    void get() {
        assertEquals(1, LocalizedMapUtil.get(map, "fr"));
        assertEquals(1, LocalizedMapUtil.get(map, "fr-FR"));
        assertEquals(2, LocalizedMapUtil.get(map, "fr-CA"));
        assertEquals(3, LocalizedMapUtil.get(map, "en-US"));
        assertNull(LocalizedMapUtil.get(map, "en"));
        assertNull(LocalizedMapUtil.get(map, "en-CA"));
        assertNull(LocalizedMapUtil.get(map, null));
        assertEquals(0, LocalizedMapUtil.get(map, "en", 0));
    }
}