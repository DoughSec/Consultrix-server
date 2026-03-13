package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Submission;
import com.consultrix.consultrixserver.model.dto.submissionDTO.SubmissionRequestDto;
import com.consultrix.consultrixserver.model.dto.submissionDTO.SubmissionResponseDto;
import com.consultrix.consultrixserver.security.SecurityUtils;
import com.consultrix.consultrixserver.service.SubmissionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/submissions")
public class SubmissionController {
    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    //create Submission record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public SubmissionResponseDto create(@RequestBody SubmissionRequestDto request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        // Students can only create submissions for themselves;
        // instructors/admins can create on behalf of any student
        Integer studentUserId = SecurityUtils.isInstructorOrAdmin()
                ? (request.getStudentUserId() != null ? request.getStudentUserId() : currentUserId.intValue())
                : currentUserId.intValue();

        return submissionService.create(
                request.getAssignmentId(),
                studentUserId,
                request.getSubmittedAt(),
                request.getContentUrl()
        );
    }

    //get all Submission records
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Submission> getAll() {
        return submissionService.listAll();
    }

    //get Submission by id
    @GetMapping("/{submissionId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public Submission getSubmissionById(@PathVariable("submissionId") Integer submissionId) {
        return submissionService.getById(submissionId);
    }

    //get Submission records by assignment
    @GetMapping("/assignment/{assignmentId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Submission> getSubmissionByAssignment(@PathVariable("assignmentId") Integer assignmentId) {
        return submissionService.listByAssignment(assignmentId);
    }

    //get Submission records by student
    @GetMapping("/student/{studentUserId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Submission> getSubmissionByStudent(@PathVariable("studentUserId") Integer studentUserId) {
        return submissionService.listByStudent(studentUserId);
    }

    //get Submission records by status
    @GetMapping("/status/{status}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Submission> getSubmissionByStatus(@PathVariable("status") String status) {
        return submissionService.listByStatus(status);
    }

    //update Submission record
    @PutMapping("/{submissionId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public SubmissionResponseDto updateSubmission(@PathVariable("submissionId") Integer submissionId, @RequestBody SubmissionRequestDto request) {
        // Students can only update their own submissions;
        // instructors/admins can update any submission
        if (!SecurityUtils.isInstructorOrAdmin()) {
            Long currentUserId = SecurityUtils.getCurrentUserId();
            Submission existing = submissionService.getById(submissionId);
            if (!existing.getStudent().getId().equals(currentUserId.intValue())) {
                throw new IllegalArgumentException("You can only update your own submissions");
            }
        }

        return submissionService.update(submissionId, request);
    }

    //delete Submission record
    @DeleteMapping("/{submissionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public void deleteSubmission(@PathVariable("submissionId") Integer submissionId) {
        submissionService.delete(submissionId);
    }

}

