package com.mediscreen.assessment;

import lombok.Generated;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {"com.mediscreen.assessment", "com.mediscreen.common.spring"})
@ConfigurationPropertiesScan(basePackages = {"com.mediscreen.assessment.properties", "com.mediscreen.common.spring.properties"})
public class AssessmentApp {
    @Generated
    public static void main(String[] args) {
        SpringApplication.run(AssessmentApp.class, args);
    }
}
