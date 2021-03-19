package com.mediscreen.patients.util;

import java.util.Map;
import lombok.experimental.UtilityClass;

@UtilityClass
public class LocalizedMapUtil {
    public static <T> T get(Map<String, T> map, String locale) {
        return get(map, locale, null);
    }

    public static <T> T get(Map<String, T> map, String locale, T fallback) {
        if (locale != null) {
            T ret = map.get(locale);
            if (ret != null) {
                return ret;
            }
            if (locale.length() > 2 && locale.charAt(2) == '-') {
                ret = map.get(locale.substring(0, 2));
                if (ret != null) {
                    return ret;
                }
            }
        }
        return fallback;
    }
}
