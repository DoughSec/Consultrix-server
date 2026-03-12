package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Cohort;
import com.consultrix.consultrixserver.model.Module;
import com.consultrix.consultrixserver.model.dto.moduleDTO.ModuleRequestDto;
import com.consultrix.consultrixserver.model.dto.moduleDTO.ModuleResponseDto;
import com.consultrix.consultrixserver.repository.CohortRepository;
import com.consultrix.consultrixserver.repository.ModuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final CohortRepository cohortRepository;

    public ModuleService(ModuleRepository moduleRepository, CohortRepository cohortRepository) {
        this.moduleRepository = moduleRepository;
        this.cohortRepository = cohortRepository;
    }

    // create Module
    public ModuleResponseDto create(Integer cohortId, String title, String description, java.time.LocalDate startDate, java.time.LocalDate endDate, Integer orderIndex) {
        if (cohortId == null) {
            throw new IllegalArgumentException("cohortId is required");
        }

        Cohort cohort = cohortRepository.findById(cohortId)
                .orElseThrow(() -> new IllegalArgumentException("Cohort not found: " + cohortId));

        Module module = new Module();
        module.setCohort(cohort);
        module.setTitle(title);
        module.setDescription(description);
        module.setStartDate(startDate);
        module.setEndDate(endDate);
        module.setOrderIndex(orderIndex != null ? orderIndex : 0);

        moduleRepository.save(module);

        ModuleResponseDto responseDto = new ModuleResponseDto();
        responseDto.setModuleId(module.getId());
        responseDto.setCohortId(module.getCohort().getId());
        responseDto.setTitle(module.getTitle());
        responseDto.setDescription(module.getDescription());
        responseDto.setStartDate(module.getStartDate());
        responseDto.setEndDate(module.getEndDate());
        responseDto.setOrderIndex(module.getOrderIndex());

        return responseDto;
    }

    // getAll
    @Transactional(readOnly = true)
    public List<Module> getAll() {
        return moduleRepository.findAll();
    }

    // getById
    @Transactional(readOnly = true)
    public Module getById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("moduleId is required");
        }
        return moduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Module not found: " + id));
    }

    // getByCohort
    @Transactional(readOnly = true)
    public List<Module> listByCohort(Integer cohortId) {
        if (cohortId == null) {
            throw new IllegalArgumentException("cohortId is required");
        }
        return moduleRepository.findByCohortIdOrderByOrderIndexAsc(cohortId);
    }

    // update Module
    public ModuleResponseDto update(Integer id, ModuleRequestDto updated) {
        Module existing = getById(id);

        existing.setTitle(updated.getTitle());
        existing.setDescription(updated.getDescription());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setOrderIndex(updated.getOrderIndex());

        moduleRepository.save(existing);

        ModuleResponseDto responseDto = new ModuleResponseDto();
        responseDto.setModuleId(existing.getId());
        responseDto.setCohortId(existing.getCohort() != null ? existing.getCohort().getId() : null);
        responseDto.setTitle(existing.getTitle());
        responseDto.setDescription(existing.getDescription());
        responseDto.setStartDate(existing.getStartDate());
        responseDto.setEndDate(existing.getEndDate());
        responseDto.setOrderIndex(existing.getOrderIndex());

        return responseDto;
    }

    // delete Module
    public void delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("moduleId is required");
        }
        if (!moduleRepository.existsById(id)) {
            throw new IllegalArgumentException("Module not found: " + id);
        }
        moduleRepository.deleteById(id);
    }
}