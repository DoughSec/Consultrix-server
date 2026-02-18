package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByModuleId(int moduleId);
}
