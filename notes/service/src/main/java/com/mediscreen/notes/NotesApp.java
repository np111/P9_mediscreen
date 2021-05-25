package com.mediscreen.notes;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = {"com.mediscreen.notes", "com.mediscreen.common.spring"})
@ConfigurationPropertiesScan(basePackages = {"com.mediscreen.notes.properties", "com.mediscreen.common.spring.properties"})
@EnableMongock
public class NotesApp {
    public static void main(String[] args) {
        SpringApplication.run(NotesApp.class, args);
    }
}
