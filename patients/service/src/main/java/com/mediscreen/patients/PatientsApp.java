package com.mediscreen.patients;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {"com.mediscreen.patients", "com.mediscreen.common.spring"})
@ConfigurationPropertiesScan(basePackages = {"com.mediscreen.patients.properties", "com.mediscreen.common.spring.properties"})
public class PatientsApp {
    @Generated
    public static void main(String[] args) {
        SpringApplication.run(PatientsApp.class, args);
    }
}
