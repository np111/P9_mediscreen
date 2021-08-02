package com.mediscreen.notes;

import com.mediscreen.notes.mock.EnabledIfIntegrationTest;
import com.mediscreen.notes.mock.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@EnabledIfIntegrationTest
@SpringBootTest
@Import({TestConfig.class})
class NotesAppTest {
    @Test
    void contextLoads() {
    }
}