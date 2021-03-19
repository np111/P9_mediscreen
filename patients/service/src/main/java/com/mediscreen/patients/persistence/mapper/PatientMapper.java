package com.mediscreen.patients.persistence.mapper;

import com.mediscreen.common.spring.config.MapperConfig;
import com.mediscreen.patients.api.model.Patient;
import com.mediscreen.patients.persistence.entity.PatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface PatientMapper {
    Patient toModel(PatientEntity entity);

    PatientEntity fromModel(Patient model);

    void fromModel(@MappingTarget PatientEntity target, Patient model);
}
