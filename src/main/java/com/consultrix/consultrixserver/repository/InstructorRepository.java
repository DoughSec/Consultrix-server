package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}
