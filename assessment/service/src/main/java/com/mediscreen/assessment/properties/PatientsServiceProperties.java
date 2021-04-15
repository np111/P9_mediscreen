package com.mediscreen.assessment.properties;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "mediscreen.patients-service")
@Data
@Validated
public class PatientsServiceProperties {
    @NotEmpty
    private String baseUrl;

    @NotNull
    @Min(16)
    private Integer maxConcurrentRequests;
}
