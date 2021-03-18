package com.mediscreen.common.util;

import java.util.Random;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UuidUtil {
    /**
     * Retrieve a type 4 (pseudo randomly generated) UUID.
     *
     * @return A randomly generated UUID
     */
    public static UUID randomUUID(Random random) {
        return randomUUID(random.nextLong(), random.nextLong());
    }

    /**
     * Retrieve a type 4 (pseudo randomly generated) UUID whose MSB is partially based on the current timestamp
     * (so the generated UUIDs will be sequential).
     *
     * @return A sequential randomly generated UUID
     */
    public static UUID sequentialUUID(Random random) {
        return randomUUID(
                (System.currentTimeMillis() << 16L) | (random.nextInt() & 0x000000000000FFFFL),
                random.nextLong());
    }

    private static UUID randomUUID(long msb, long lsb) {
        return new UUID(
                // set to version 4:
                msb & 0xFFFFFFFFFFFF0FFFL | 0x0000000000004000L,
                // set to IETF variant:
                lsb & 0x3FFFFFFFFFFFFFFFL | 0x8000000000000000L);
    }
}
