package com.mediscreen.patients.mock;

import com.mediscreen.common.spring.properties.SecurityProperties;
import com.mediscreen.patients.persistence.mapper.PatientMapperImpl;
import java.util.Collections;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
    @Bean
    public SecurityProperties getSecurityProperties() {
        SecurityProperties ret = new SecurityProperties();
        ret.setAllowedOrigins(Collections.emptyList());
        return ret;
    }

    @Bean
    public PatientMockData getPatientMockData() {
        return new PatientMockData(new PatientMapperImpl());
    }
}
