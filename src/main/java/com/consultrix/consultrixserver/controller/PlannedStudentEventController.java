package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.dto.plannedEventDTO.PlannedStudentEventRequestDto;
import com.consultrix.consultrixserver.model.dto.plannedEventDTO.PlannedStudentEventResponseDto;
import com.consultrix.consultrixserver.security.SecurityUtils;
import com.consultrix.consultrixserver.service.PlannedStudentEventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/consultrix/planned-events")
public class PlannedStudentEventController {

    private final PlannedStudentEventService service;

    public PlannedStudentEventController(PlannedStudentEventService service) {
        this.service = service;
    }

    /** Student: create or update a planned event (upsert by date). */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public PlannedStudentEventResponseDto upsert(@RequestBody PlannedStudentEventRequestDto dto) {
        Long studentId = SecurityUtils.getCurrentUserId();
        return service.upsert(studentId.intValue(), dto);
    }

    /** Student: get all their own planned events. */
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public List<PlannedStudentEventResponseDto> getMyEvents() {
        Long studentId = SecurityUtils.getCurrentUserId();
        return service.getMyEvents(studentId.intValue());
    }

    /** Student: delete one of their own planned events. */
    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public void delete(@PathVariable Integer eventId) {
        Long studentId = SecurityUtils.getCurrentUserId();
        service.delete(studentId.intValue(), eventId);
    }

    /** Instructor/Admin: get all planned events for a given date (for attendance annotations). */
    @GetMapping("/date/{date}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<PlannedStudentEventResponseDto> getEventsForDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return service.getEventsForDate(date);
    }
}
