package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.PlannedStudentEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlannedStudentEventRepository extends JpaRepository<PlannedStudentEvent, Integer> {

    List<PlannedStudentEvent> findByStudentId(Integer studentId);

    List<PlannedStudentEvent> findByEventDate(LocalDate eventDate);

    List<PlannedStudentEvent> findByEventDateBetween(LocalDate start, LocalDate end);

    Optional<PlannedStudentEvent> findByStudentIdAndEventDate(Integer studentId, LocalDate eventDate);
}
