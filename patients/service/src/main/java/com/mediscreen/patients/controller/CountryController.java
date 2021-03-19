package com.mediscreen.patients.controller;

import com.mediscreen.patients.api.model.Countries;
import com.mediscreen.patients.service.CountryService;
import javax.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/country")
@Validated
public class CountryController {
    private final CountryService countryService;

    @RequestMapping(method = RequestMethod.GET)
    public Countries listCountries(
            @RequestParam(name = "locale", required = false) @Size(max = 255) String locale
    ) {
        return countryService.getLocalizedCountries(locale);
    }
}
