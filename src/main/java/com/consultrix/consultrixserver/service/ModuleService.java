package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Module;
import com.consultrix.consultrixserver.repository.ModuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    // CREATE
    public Module create(Module module) {
        if (module == null) {
            throw new IllegalArgumentException("Module must not be null");
        }

        return moduleRepository.save(module);
    }

    // GET BY ID
    public Module getById(Integer id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Module not found"));
    }

    // GET ALL
    public List<Module> getAll() {
        return moduleRepository.findAll();
    }

    // UPDATE (SAFE)
    public Module update(Module module) {
        if (module == null || module.getId() == null) {
            throw new IllegalArgumentException("Module and id must not be null");
        }

        Module existing = getById(module.getId());

        existing.setTitle(module.getTitle());
        existing.setDescription(module.getDescription());
        existing.setStartDate(module.getStartDate());
        existing.setEndDate(module.getEndDate());

        return moduleRepository.save(existing);
    }

    // DELETE
    public void delete(Integer id) {
        Module module = getById(id);
        moduleRepository.delete(module);
    }
}