package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentId(int studentUserId);
    List<Attendance> findByCohortId(int cohortId);

    Optional<Attendance> findByCohortIdAndStudentIdAndAttendanceDate(int cohortId, int studentUserId, LocalDate attendanceDate);
}
