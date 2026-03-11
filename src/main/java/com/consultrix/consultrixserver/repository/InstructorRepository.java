package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Integer> {
    Instructor findByUser_Id(Integer userId);
}
