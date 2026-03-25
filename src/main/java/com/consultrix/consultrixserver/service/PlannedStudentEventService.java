package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.PlannedStudentEvent;
import com.consultrix.consultrixserver.model.Student;
import com.consultrix.consultrixserver.model.dto.plannedEventDTO.PlannedStudentEventRequestDto;
import com.consultrix.consultrixserver.model.dto.plannedEventDTO.PlannedStudentEventResponseDto;
import com.consultrix.consultrixserver.repository.PlannedStudentEventRepository;
import com.consultrix.consultrixserver.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PlannedStudentEventService {

    private final PlannedStudentEventRepository repository;
    private final StudentRepository studentRepository;

    public PlannedStudentEventService(PlannedStudentEventRepository repository,
                                      StudentRepository studentRepository) {
        this.repository = repository;
        this.studentRepository = studentRepository;
    }

    /** Student creates or updates a planned event for a given date. */
    public PlannedStudentEventResponseDto upsert(Integer studentId, PlannedStudentEventRequestDto dto) {
        if (studentId == null || dto.getEventDate() == null || dto.getEventType() == null) {
            throw new IllegalArgumentException("studentId, eventDate, and eventType are required");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        PlannedStudentEvent event = repository
                .findByStudentIdAndEventDate(studentId, dto.getEventDate())
                .orElseGet(PlannedStudentEvent::new);

        event.setStudent(student);
        event.setEventDate(dto.getEventDate());
        event.setEventType(dto.getEventType().toUpperCase());
        event.setNote(dto.getNote());

        repository.save(event);
        return toDto(event);
    }

    /** Student deletes their planned event for a date. */
    public void delete(Integer studentId, Integer eventId) {
        PlannedStudentEvent event = repository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Planned event not found: " + eventId));
        if (!event.getStudent().getId().equals(studentId)) {
            throw new IllegalArgumentException("Not authorized to delete this event");
        }
        repository.delete(event);
    }

    /** Student reads their own planned events. */
    @Transactional(readOnly = true)
    public List<PlannedStudentEventResponseDto> getMyEvents(Integer studentId) {
        return repository.findByStudentId(studentId).stream()
                .map(this::toDto)
                .toList();
    }

    /** Instructor reads all planned events for a specific date (to show annotations). */
    @Transactional(readOnly = true)
    public List<PlannedStudentEventResponseDto> getEventsForDate(LocalDate date) {
        return repository.findByEventDate(date).stream()
                .map(this::toDto)
                .toList();
    }

    private PlannedStudentEventResponseDto toDto(PlannedStudentEvent e) {
        PlannedStudentEventResponseDto dto = new PlannedStudentEventResponseDto();
        dto.setId(e.getId());
        dto.setStudentUserId(e.getStudent().getId());
        dto.setStudentFirstName(e.getStudent().getFirstName());
        dto.setStudentLastName(e.getStudent().getLastName());
        dto.setEventDate(e.getEventDate());
        dto.setEventType(e.getEventType());
        dto.setNote(e.getNote());
        dto.setCreatedAt(e.getCreatedAt());
        return dto;
    }
}
