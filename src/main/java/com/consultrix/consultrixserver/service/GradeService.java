package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Grade;
import com.consultrix.consultrixserver.repository.GradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;

    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    // CREATE
    public Grade create(Grade grade) {
        if (grade.getId() != null && gradeRepository.existsById(grade.getId())) {
            throw new IllegalArgumentException("Grade already exists");
        }

        return gradeRepository.save(grade);
    }

    // GET BY ID
    public Grade getById(Integer id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grade not found with id: " + id));
    }

    // GET BY SUBMISSION
    public List<Grade> getBySubmissionId(Integer submissionId) {
        return gradeRepository.findBySubmissionId(submissionId);
    }

    // UPDATE
    public Grade update(Integer id, Grade updated) {
        Grade existing = getById(id);

        existing.setScore(updated.getScore());

        return gradeRepository.save(existing);
    }

    // DELETE
    public void delete(Integer id) {
        Grade grade = getById(id);
        gradeRepository.delete(grade);
    }
}