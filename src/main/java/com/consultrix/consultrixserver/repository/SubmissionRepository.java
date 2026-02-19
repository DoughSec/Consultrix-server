package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Integer> {
    List<Submission> findByAssignmentId(Integer assignmentId);
    List<Submission> findByStudentId(Integer studentUserId);
    List<Submission> findByStatus(String status);
    Optional<Submission> findByAssignmentIdAndStudentId(Integer assignmentId, Integer studentUserId);
}
