package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByModuleId(int moduleId);
}
