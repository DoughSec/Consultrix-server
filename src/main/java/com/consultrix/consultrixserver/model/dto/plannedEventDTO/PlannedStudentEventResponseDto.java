package com.consultrix.consultrixserver.model.dto.plannedEventDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PlannedStudentEventResponseDto {
    private Integer id;
    private Integer studentUserId;
    private String studentFirstName;
    private String studentLastName;
    private LocalDate eventDate;
    /** LATE | REMOTE | OFF */
    private String eventType;
    private String note;
    private LocalDateTime createdAt;
}
