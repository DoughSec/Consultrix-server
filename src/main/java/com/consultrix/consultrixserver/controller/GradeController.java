package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Grade;
import com.consultrix.consultrixserver.model.dto.gradeDTO.GradeRequestDto;
import com.consultrix.consultrixserver.model.dto.gradeDTO.GradeResponseDto;
import com.consultrix.consultrixserver.service.GradeService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/grades")
public class GradeController {
    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    //create Grade record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public GradeResponseDto create(@RequestBody GradeRequestDto request) {
        return gradeService.create(
                request.getSubmissionId(),
                request.getInstructorUserId(),
                request.getScore(),
                request.getFeedback()
        );
    }

    //get all Grade records
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Grade> getAll() {
        return gradeService.listAll();
    }

    //get Grade by id
    @GetMapping("/{gradeId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public Grade getGradeById(@PathVariable("gradeId") Integer gradeId) {
        return gradeService.getById(gradeId);
    }

    //get Grade records by submission
    @GetMapping("/submission/{submissionId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Grade> getGradeBySubmission(@PathVariable("submissionId") Integer submissionId) {
        return gradeService.listBySubmission(submissionId);
    }

    //get Grade records by instructor
    @GetMapping("/instructor/{instructorUserId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Grade> getGradeByInstructor(@PathVariable("instructorUserId") Integer instructorUserId) {
        return gradeService.listByInstructor(instructorUserId);
    }

    //update Grade record
    @PutMapping("/{gradeId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public GradeResponseDto updateGrade(@PathVariable("gradeId") Integer gradeId, @RequestBody GradeRequestDto request) {
        return gradeService.update(gradeId, request);
    }

    //delete Grade record
    @DeleteMapping("/{gradeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public void deleteGrade(@PathVariable("gradeId") Integer gradeId) {
        gradeService.delete(gradeId);
    }

}

