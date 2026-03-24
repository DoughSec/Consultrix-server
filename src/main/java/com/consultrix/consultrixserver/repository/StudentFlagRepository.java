package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.StudentFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentFlagRepository extends JpaRepository<StudentFlag, Integer> {

    List<StudentFlag> findByStudentId(Integer studentId);

    List<StudentFlag> findByInstructorId(Integer instructorId);

    List<StudentFlag> findByResolved(boolean resolved);

    List<StudentFlag> findByStudentIdAndResolved(Integer studentId, boolean resolved);

}
