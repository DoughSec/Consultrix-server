package com.consultrix.consultrixserver.model.dto.plannedEventDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PlannedStudentEventRequestDto {
    private LocalDate eventDate;
    /** LATE | REMOTE | OFF */
    private String eventType;
    private String note;
}
