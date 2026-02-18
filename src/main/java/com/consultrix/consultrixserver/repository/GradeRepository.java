package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findBySubmissionId(int submissionId);
    List<Grade> findByInstructorId(int instructorUserId);

    Optional<Grade> findBySubmissionIdAndInstructorId(int submissionId, int instructorUserId);

}
