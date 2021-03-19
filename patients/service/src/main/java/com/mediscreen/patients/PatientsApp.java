package com.mediscreen.patients;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.mediscreen.patients", "com.mediscreen.common.spring"})
public class PatientsApp {
    public static void main(String[] args) {
        SpringApplication.run(PatientsApp.class, args);
    }
}
