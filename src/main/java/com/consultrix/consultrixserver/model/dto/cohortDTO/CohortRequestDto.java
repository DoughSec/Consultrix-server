package com.consultrix.consultrixserver.model.dto.cohortDTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CohortRequestDto {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer capacity = 0;
    // RECRUITING / INTERVIEWING / ACTIVE / COMPLETED / ARCHIVED
    private String status;
}
