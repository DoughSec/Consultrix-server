package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Grade;
import com.consultrix.consultrixserver.model.Instructor;
import com.consultrix.consultrixserver.model.Submission;
import com.consultrix.consultrixserver.model.dto.gradeDTO.GradeRequestDto;
import com.consultrix.consultrixserver.model.dto.gradeDTO.GradeResponseDto;
import com.consultrix.consultrixserver.repository.GradeRepository;
import com.consultrix.consultrixserver.repository.InstructorRepository;
import com.consultrix.consultrixserver.repository.SubmissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class GradeService {

    private final GradeRepository gradeRepository;
    private final SubmissionRepository submissionRepository;
    private final InstructorRepository instructorRepository;

    public GradeService(GradeRepository gradeRepository, SubmissionRepository submissionRepository, InstructorRepository instructorRepository) {
        this.gradeRepository = gradeRepository;
        this.submissionRepository = submissionRepository;
        this.instructorRepository = instructorRepository;
    }

    // create Grade
    public GradeResponseDto create(Integer submissionId, Integer instructorUserId, BigDecimal score, String feedback) {
        if (submissionId == null || instructorUserId == null) {
            throw new IllegalArgumentException("submissionId and instructorUserId are required");
        }

        // Prevent duplicates for same submission + instructor
        if (gradeRepository.findBySubmissionIdAndInstructorId(submissionId, instructorUserId).isPresent()) {
            throw new IllegalArgumentException("Grade already exists for this submission by this instructor");
        }

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException("Submission not found: " + submissionId));

        Instructor instructor = instructorRepository.findById(instructorUserId)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found: " + instructorUserId));

        Grade grade = new Grade();
        grade.setSubmission(submission);
        grade.setInstructor(instructor);
        grade.setScore(score);
        grade.setFeedback(feedback);

        gradeRepository.save(grade);

        GradeResponseDto responseDto = new GradeResponseDto();
        responseDto.setGradeId(grade.getId());
        responseDto.setSubmissionId(grade.getSubmission().getId());
        responseDto.setInstructorUserId(grade.getInstructor().getId());
        responseDto.setScore(grade.getScore());
        responseDto.setFeedback(grade.getFeedback());

        return responseDto;
    }

    // getAll
    @Transactional(readOnly = true)
    public List<Grade> listAll() {
        return gradeRepository.findAll();
    }

    // getById
    @Transactional(readOnly = true)
    public Grade getById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("gradeId is required");
        }
        return gradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Grade not found with id: " + id));
    }

    // getBySubmission
    @Transactional(readOnly = true)
    public List<Grade> listBySubmission(Integer submissionId) {
        if (submissionId == null) {
            throw new IllegalArgumentException("submissionId is required");
        }
        return gradeRepository.findBySubmissionId(submissionId);
    }

    // getByInstructor
    @Transactional(readOnly = true)
    public List<Grade> listByInstructor(Integer instructorUserId) {
        if (instructorUserId == null) {
            throw new IllegalArgumentException("instructorUserId is required");
        }
        return gradeRepository.findByInstructorId(instructorUserId);
    }

    // update Grade
    public GradeResponseDto update(Integer id, GradeRequestDto updated) {
        Grade existing = getById(id);

        existing.setScore(updated.getScore());
        existing.setFeedback(updated.getFeedback());

        gradeRepository.save(existing);

        GradeResponseDto responseDto = new GradeResponseDto();
        responseDto.setGradeId(existing.getId());
        responseDto.setSubmissionId(existing.getSubmission().getId());
        responseDto.setInstructorUserId(existing.getInstructor().getId());
        responseDto.setScore(existing.getScore());
        responseDto.setFeedback(existing.getFeedback());

        return responseDto;
    }

    // delete Grade
    public void delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("gradeId is required");
        }
        if (!gradeRepository.existsById(id)) {
            throw new IllegalArgumentException("Grade not found with id: " + id);
        }
        gradeRepository.deleteById(id);
    }
}