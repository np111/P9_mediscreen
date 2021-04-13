package com.mediscreen.assessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.mediscreen.assessment", "com.mediscreen.common.spring"})
public class AssessmentApp {
    public static void main(String[] args) {
        SpringApplication.run(AssessmentApp.class, args);
    }
}
