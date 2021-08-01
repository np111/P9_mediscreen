package com.mediscreen.patients;

import com.mediscreen.patients.mock.EnabledIfIntegrationTest;
import com.mediscreen.patients.mock.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@EnabledIfIntegrationTest
@SpringBootTest
@Import({TestConfig.class})
class PatientsAppTest {
    @Test
    void contextLoads() {
    }
}
