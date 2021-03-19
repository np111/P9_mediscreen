package com.mediscreen.common.spring.config;

import org.mapstruct.Builder;
import org.mapstruct.ReportingPolicy;

@org.mapstruct.MapperConfig(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        typeConversionPolicy = ReportingPolicy.ERROR,
        builder = @Builder(disableBuilder = true)
)
public interface MapperConfig {
}
