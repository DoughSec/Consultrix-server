package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findCohortById(Long cohortId);
    List<Student> findByPipelineStage(String pipelineStage);
    List<Student> findByGraduationStatus(String graduationStatus);
}
