package com.mediscreen.notes;

import com.github.cloudyrock.spring.v5.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.mediscreen.notes", "com.mediscreen.common.spring"})
@EnableMongock
public class NotesApp {
    public static void main(String[] args) {
        SpringApplication.run(NotesApp.class, args);
    }
}
