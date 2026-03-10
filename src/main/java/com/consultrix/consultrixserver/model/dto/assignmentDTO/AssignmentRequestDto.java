package com.consultrix.consultrixserver.model.dto.assignmentDTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class AssignmentRequestDto {
    private Integer moduleId;
    private String title;
    private String description;
    private LocalDate dueDate;
    private LocalDateTime dueTime;
    private BigDecimal maxScore;
}
