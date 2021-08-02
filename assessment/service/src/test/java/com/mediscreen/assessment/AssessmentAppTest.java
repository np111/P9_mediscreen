package com.mediscreen.assessment;

import com.mediscreen.assessment.mock.EnabledIfIntegrationTest;
import com.mediscreen.assessment.mock.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@EnabledIfIntegrationTest
@SpringBootTest
@Import({TestConfig.class})
class AssessmentAppTest {
    @Test
    void contextLoads() {
    }
}