package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Module;
import com.consultrix.consultrixserver.model.dto.moduleDTO.ModuleRequestDto;
import com.consultrix.consultrixserver.model.dto.moduleDTO.ModuleResponseDto;
import com.consultrix.consultrixserver.service.ModuleService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/modules")
public class ModuleController {
    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    //create Module record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public ModuleResponseDto create(@RequestBody ModuleRequestDto request) {
        return moduleService.create(
                request.getCohortId(),
                request.getTitle(),
                request.getDescription(),
                request.getStartDate(),
                request.getEndDate(),
                request.getOrderIndex()
        );
    }

    //get all Module records
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Module> getAll() {
        return moduleService.getAll();
    }

    //get Module by id
    @GetMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public Module getModuleById(@PathVariable("moduleId") Integer moduleId) {
        return moduleService.getById(moduleId);
    }

    //get Module records by cohort
    @GetMapping("/cohort/{cohortId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Module> getModuleByCohort(@PathVariable("cohortId") Integer cohortId) {
        return moduleService.listByCohort(cohortId);
    }

    //update Module record
    @PutMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public ModuleResponseDto updateModule(@PathVariable("moduleId") Integer moduleId, @RequestBody ModuleRequestDto request) {
        return moduleService.update(moduleId, request);
    }

    //delete Module record
    @DeleteMapping("/{moduleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public void deleteModule(@PathVariable("moduleId") Integer moduleId) {
        moduleService.delete(moduleId);
    }

}

