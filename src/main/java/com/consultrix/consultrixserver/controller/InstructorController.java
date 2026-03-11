package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Instructor;
import com.consultrix.consultrixserver.model.dto.instructorDTO.InstructorProfileRequestDto;
import com.consultrix.consultrixserver.model.dto.instructorDTO.InstructorProfileResponseDto;
import com.consultrix.consultrixserver.security.SecurityUtils;
import com.consultrix.consultrixserver.service.InstructorService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/instructor")
public class InstructorController {
    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    //create Instructor record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public InstructorProfileResponseDto create(@RequestBody InstructorProfileRequestDto request) {
        return instructorService.create(
                request.getUserId(),
                request.getFirstName(),
                request.getLastName(),
                request.getStatus(),
                request.getEmail(),
                request.getTitle(),
                request.getSpecialty(),
                request.getOfficeHours()
        );
    }

    //get all Instructor records
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Instructor> getAll() {
        return instructorService.getAll();
    }

    //get Instructor by id
    @GetMapping("/{instructorId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Instructor getInstructorById(@PathVariable("instructorId") Integer instructorId) {
        return instructorService.getById(instructorId);
    }

    //update Instructor record
    @PutMapping("/{instructorId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    public InstructorProfileResponseDto updateInstructor(@PathVariable("instructorId") Integer instructorId, @RequestBody InstructorProfileRequestDto instructor) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        return instructorService.update(instructorId, currentUserId.intValue(), instructor);
    }

    //delete Instructor record
    @DeleteMapping("/{instructorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteInstructor(@PathVariable("instructorId") Integer instructorId) {
        instructorService.delete(instructorId);
    }

}