package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.dto.flagDTO.StudentFlagRequestDto;
import com.consultrix.consultrixserver.model.dto.flagDTO.StudentFlagResponseDto;
import com.consultrix.consultrixserver.security.SecurityUtils;
import com.consultrix.consultrixserver.service.StudentFlagService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/flags")
public class StudentFlagController {

    private final StudentFlagService studentFlagService;

    public StudentFlagController(StudentFlagService studentFlagService) {
        this.studentFlagService = studentFlagService;
    }

    // Create a new flag for a student; instructor ID is derived from the JWT principal
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR', 'ROLE_ADMIN')")
    public StudentFlagResponseDto createFlag(@RequestBody StudentFlagRequestDto request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return studentFlagService.createFlag(currentUserId.intValue(), request);
    }

    // Resolve an existing flag by its ID
    @PatchMapping("/{id}/resolve")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR', 'ROLE_ADMIN')")
    public StudentFlagResponseDto resolveFlag(@PathVariable("id") Integer id) {
        return studentFlagService.resolveFlag(id);
    }

    // Get all flags for a specific student
    @GetMapping("/student/{studentId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR', 'ROLE_ADMIN')")
    public List<StudentFlagResponseDto> getFlagsForStudent(@PathVariable("studentId") Integer studentId) {
        return studentFlagService.getFlagsForStudent(studentId);
    }

    // Get all unresolved flags across all students (admin only)
    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<StudentFlagResponseDto> getActiveFlags() {
        return studentFlagService.getActiveFlags();
    }

    // Get all flags created by the currently authenticated instructor
    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    public List<StudentFlagResponseDto> getMyFlags() {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return studentFlagService.getFlagsForInstructor(currentUserId.intValue());
    }

}
