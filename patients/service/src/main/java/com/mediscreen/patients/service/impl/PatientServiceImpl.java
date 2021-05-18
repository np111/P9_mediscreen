package com.mediscreen.patients.service.impl;

import com.mediscreen.common.util.UuidUtil;
import com.mediscreen.patients.api.model.Patient;
import com.mediscreen.patients.exception.PatientNotFoundException;
import com.mediscreen.patients.persistence.entity.PatientEntity;
import com.mediscreen.patients.persistence.mapper.PatientMapper;
import com.mediscreen.patients.persistence.repository.PatientRepository;
import com.mediscreen.patients.service.PatientService;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Patient> list() {
        return patientRepository.findAll().stream().map(patientMapper::toModel).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Patient get(UUID id) throws PatientNotFoundException {
        PatientEntity entity = patientRepository.findById(id).orElse(null);
        if (entity == null) {
            throw new PatientNotFoundException(id);
        }
        return patientMapper.toModel(entity);
    }

    @Override
    @Transactional
    public Patient create(Patient patient) {
        PatientEntity entity = patientMapper.fromModel(patient);
        entity.setId(UuidUtil.sequentialUUID(ThreadLocalRandom.current()));
        return patientMapper.toModel(patientRepository.save(entity));
    }

    @Override
    @Transactional
    public Patient update(UUID id, Patient patient) throws PatientNotFoundException {
        PatientEntity entity = patientRepository.findById(id).orElse(null);
        if (entity == null) {
            throw new PatientNotFoundException(id);
        }
        patientMapper.fromModel(entity, patient);
        entity.setId(id);
        return patientMapper.toModel(patientRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(UUID id) throws PatientNotFoundException {
        PatientEntity entity = patientRepository.findById(id).orElse(null);
        if (entity == null) {
            throw new PatientNotFoundException(id);
        }
        patientRepository.deleteById(id);
    }
}
