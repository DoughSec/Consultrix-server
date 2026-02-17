package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findCohortById(Long cohortId);
    List<Student> findByPipelineStage(String pipelineStage);
    List<Student> findByGraduationStatus(String graduationStatus);
}
