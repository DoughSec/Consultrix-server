package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Assignment;
import com.consultrix.consultrixserver.model.dto.assignmentDTO.AssignmentRequestDto;
import com.consultrix.consultrixserver.model.dto.assignmentDTO.AssignmentResponseDto;
import com.consultrix.consultrixserver.security.SecurityUtils;
import com.consultrix.consultrixserver.service.AssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/assignment")
public class AssignmentController {
    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    //create Assignment record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public AssignmentResponseDto create(@RequestBody AssignmentRequestDto request) {
        return assignmentService.create(
                request.getModuleId(),
                request.getTitle(),
                request.getDescription(),
                request.getDueDate(),
                request.getDueTime(),
                request.getMaxScore()
        );
    }

    //get all Assignment records
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Assignment> getAll() {
        return assignmentService.getAll();
    }

    //get Assignment by id
    @GetMapping("/{assignmentId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public Assignment getAssignmentById(@PathVariable("assignmentId") Integer assignmentId) {
        return assignmentService.getById(assignmentId);
    }

    //get Assignment records by module
    @GetMapping("/module/{moduleId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Assignment> getAssignmentByModule(@PathVariable("moduleId") Integer moduleId) {
        return assignmentService.getByModule(moduleId);
    }

    //update Assignment record
    @PutMapping("/{assignmentId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public AssignmentResponseDto updateAssignment(@PathVariable("assignmentId") Integer assignmentId, @RequestBody AssignmentRequestDto request) {
        return assignmentService.update(assignmentId, request);
    }

    //delete Assignment record
    @DeleteMapping("/{assignmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public void deleteAssignment(@PathVariable("assignmentId") Integer assignmentId) {
        assignmentService.delete(assignmentId);
    }

}
