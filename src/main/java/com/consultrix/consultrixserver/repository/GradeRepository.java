package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Integer> {
    List<Grade> findBySubmissionId(Integer submissionId);
    List<Grade> findByInstructorId(Integer instructorUserId);

    Optional<Grade> findBySubmissionIdAndInstructorId(Integer submissionId, Integer instructorUserId);

}
