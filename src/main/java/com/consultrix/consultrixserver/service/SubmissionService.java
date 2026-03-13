package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Assignment;
import com.consultrix.consultrixserver.model.Student;
import com.consultrix.consultrixserver.model.Submission;
import com.consultrix.consultrixserver.model.dto.submissionDTO.SubmissionRequestDto;
import com.consultrix.consultrixserver.model.dto.submissionDTO.SubmissionResponseDto;
import com.consultrix.consultrixserver.repository.AssignmentRepository;
import com.consultrix.consultrixserver.repository.StudentRepository;
import com.consultrix.consultrixserver.repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final StudentRepository studentRepository;

    public SubmissionService(
            SubmissionRepository submissionRepository,
            AssignmentRepository assignmentRepository,
            StudentRepository studentRepository
    ) {
        this.submissionRepository = submissionRepository;
        this.assignmentRepository = assignmentRepository;
        this.studentRepository = studentRepository;
    }

    // Determine submission status based on submittedAt vs assignment deadline
    private String resolveStatus(Assignment assignment, LocalDateTime submittedAt) {
        if (submittedAt == null) {
            return "MISSING";
        }

        // Use dueTime if available, otherwise fall back to end-of-day on dueDate
        LocalDateTime deadline = assignment.getDueTime() != null
                ? assignment.getDueTime()
                : (assignment.getDueDate() != null
                    ? assignment.getDueDate().atTime(23, 59, 59)
                    : null);

        if (deadline != null && submittedAt.isAfter(deadline)) {
            return "LATE";
        }

        return "SUBMITTED";
    }

    // create Submission
    public SubmissionResponseDto create(Integer assignmentId, Integer studentUserId, LocalDateTime submittedAt, String contentUrl) {
        if (assignmentId == null || studentUserId == null) {
            throw new IllegalArgumentException("assignmentId and studentUserId are required");
        }

        // Prevent duplicates for same assignment + student
        if (submissionRepository.findByAssignmentIdAndStudentId(assignmentId, studentUserId).isPresent()) {
            throw new IllegalArgumentException("Submission already exists for this student on this assignment");
        }

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found: " + assignmentId));

        Student student = studentRepository.findById(studentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentUserId));

        LocalDateTime effectiveSubmittedAt = submittedAt != null ? submittedAt : LocalDateTime.now();

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setSubmittedAt(effectiveSubmittedAt);
        submission.setContentUrl(contentUrl);
        submission.setStatus(resolveStatus(assignment, effectiveSubmittedAt));

        submissionRepository.save(submission);

        SubmissionResponseDto responseDto = new SubmissionResponseDto();
        responseDto.setSubmissionId(submission.getId());
        responseDto.setAssignmentId(submission.getAssignment().getId());
        responseDto.setStudentUserId(submission.getStudent().getId());
        responseDto.setSubmittedAt(submission.getSubmittedAt());
        responseDto.setContentUrl(submission.getContentUrl());
        responseDto.setStatus(submission.getStatus());

        return responseDto;
    }

    // getAll
    @Transactional(readOnly = true)
    public List<Submission> listAll() {
        return submissionRepository.findAll();
    }

    // getByAssignment
    @Transactional(readOnly = true)
    public List<Submission> listByAssignment(Integer assignmentId) {
        if (assignmentId == null) {
            throw new IllegalArgumentException("assignmentId is required");
        }
        return submissionRepository.findByAssignmentId(assignmentId);
    }

    // getByStudent
    @Transactional(readOnly = true)
    public List<Submission> listByStudent(Integer studentUserId) {
        if (studentUserId == null) {
            throw new IllegalArgumentException("studentUserId is required");
        }
        return submissionRepository.findByStudentId(studentUserId);
    }

    // getByStatus
    @Transactional(readOnly = true)
    public List<Submission> listByStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException("status is required");
        }
        return submissionRepository.findByStatus(status);
    }

    // getById
    @Transactional(readOnly = true)
    public Submission getById(Integer submissionId) {
        if (submissionId == null) {
            throw new IllegalArgumentException("submissionId is required");
        }
        return submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found: " + submissionId));
    }

    // update Submission
    public SubmissionResponseDto update(Integer submissionId, SubmissionRequestDto updated) {
        Submission existing = getById(submissionId);

        LocalDateTime effectiveSubmittedAt = updated.getSubmittedAt() != null
                ? updated.getSubmittedAt()
                : existing.getSubmittedAt();

        existing.setSubmittedAt(effectiveSubmittedAt);
        existing.setContentUrl(updated.getContentUrl());
        existing.setStatus(resolveStatus(existing.getAssignment(), effectiveSubmittedAt));

        submissionRepository.save(existing);

        SubmissionResponseDto responseDto = new SubmissionResponseDto();
        responseDto.setSubmissionId(existing.getId());
        responseDto.setAssignmentId(existing.getAssignment().getId());
        responseDto.setStudentUserId(existing.getStudent().getId());
        responseDto.setSubmittedAt(existing.getSubmittedAt());
        responseDto.setContentUrl(existing.getContentUrl());
        responseDto.setStatus(existing.getStatus());

        return responseDto;
    }

    // delete Submission
    public void delete(Integer submissionId) {
        if (submissionId == null) {
            throw new IllegalArgumentException("submissionId is required");
        }
        if (!submissionRepository.existsById(submissionId)) {
            throw new IllegalArgumentException("Submission not found: " + submissionId);
        }
        submissionRepository.deleteById(submissionId);
    }
}
