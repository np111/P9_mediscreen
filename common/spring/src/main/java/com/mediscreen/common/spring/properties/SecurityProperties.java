package com.mediscreen.common.spring.properties;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "mediscreen.security")
@Data
@Validated
public class SecurityProperties {
    private List<@NotBlank String> allowedOrigins;
}
