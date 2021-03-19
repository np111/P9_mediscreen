package com.mediscreen.patients.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediscreen.patients.api.model.Countries;
import com.mediscreen.patients.api.model.Country;
import com.mediscreen.patients.util.LocalizedMapUtil;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class CountryService implements InitializingBean {
    private Set<String> countriesCodes;
    private @Getter Set<String> supportedLocales;
    private Countries countriesFallback;
    private Map<String, Countries> countriesByLocale;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Iso3166Country> iso3166Countries;
        try (Reader rd = new InputStreamReader(CountryService.class.getResourceAsStream("/iso3166.json"), StandardCharsets.UTF_8)) {
            iso3166Countries = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readerForListOf(Iso3166Country.class)
                    .readValue(rd);
        }

        Set<String> countriesCodes = iso3166Countries.stream().map(Iso3166Country::getAlpha2).collect(Collectors.toSet());

        Set<String> supportedLocales = iso3166Countries.stream().flatMap(country -> country.getName().keySet().stream()).collect(Collectors.toSet());

        Map<String, Countries> countriesByLocale = new HashMap<>();
        for (String locale : supportedLocales) {
            Countries.Builder countries = Countries.builder().locale(locale);
            iso3166Countries.forEach(country -> {
                String code = country.getAlpha2();
                String name = country.getName().getOrDefault(locale, country.getName().getOrDefault("en", code));
                countries.country(code, Country.builder().code(code).name(name).build());
            });
            countriesByLocale.put(locale, countries.build());
        }

        this.countriesCodes = Collections.unmodifiableSet(countriesCodes);
        this.supportedLocales = Collections.unmodifiableSet(supportedLocales);
        this.countriesFallback = Objects.requireNonNull(countriesByLocale.get("en"));
        this.countriesByLocale = Collections.unmodifiableMap(countriesByLocale);
    }

    public boolean isCountryCode(String value) {
        return countriesCodes.contains(value);
    }

    public Countries getLocalizedCountries(String locale) {
        return LocalizedMapUtil.get(countriesByLocale, locale, countriesFallback);
    }

    @Data
    private static class Iso3166Country {
        private String alpha2;
        private Map<String, String> name;
    }
}
