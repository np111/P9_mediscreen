package com.mediscreen.patients.controller;

import com.mediscreen.patients.api.model.Countries;
import com.mediscreen.patients.api.model.Country;
import com.mediscreen.patients.mock.TestConfig;
import com.mediscreen.patients.service.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(CountryController.class)
@Import({TestConfig.class})
class CountryControllerTest extends AbstractControllerTest {
    @MockBean
    private CountryService countryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listCountries() throws Exception {
        doAnswer(m -> Countries.builder()
                .locale(m.getArgument(0))
                .country("us", Country.builder().name("us").name("United States").build())
                .country("fr", Country.builder().name("fr").name("France").build())
                .build())
                .when(countryService).getLocalizedCountries(any());

        toMatchSnapshot(
                mockMvc.perform(get("/country").queryParam("locale", "en")).andReturn(),
                mockMvc.perform(get("/country").queryParam("locale", "fr")).andReturn(),
                mockMvc.perform(get("/country").queryParam("locale", "too_long".repeat(50))).andReturn()
        );
    }
}