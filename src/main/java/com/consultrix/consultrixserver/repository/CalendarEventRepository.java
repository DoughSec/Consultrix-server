package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Integer> {

    // Events for a specific cohort OR events with no cohort (global)
    @Query("SELECT e FROM CalendarEvent e WHERE e.cohort.id = :cohortId OR e.cohort IS NULL ORDER BY e.startTime ASC")
    List<CalendarEvent> findByCohortIdOrGlobal(@Param("cohortId") Integer cohortId);

    List<CalendarEvent> findAllByOrderByStartTimeAsc();
}
