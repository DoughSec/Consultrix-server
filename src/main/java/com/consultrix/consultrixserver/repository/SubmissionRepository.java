package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByAssignmentId(int assignmentId);
    List<Submission> findByStudentId(int studentUserId);
    List<Submission> findByStatus(String status);
    Optional<Submission> findByAssignmentIdAndStudentId(int assignmentId, int studentUserId);
}
