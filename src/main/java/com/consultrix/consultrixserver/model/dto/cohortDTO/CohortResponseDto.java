package com.consultrix.consultrixserver.model.dto.cohortDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CohortResponseDto {
    private Integer cohortId;
    private Integer facilityId;
    private Integer primaryInstructorUserId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer capacity;
    // RECRUITING / INTERVIEWING / ACTIVE / COMPLETED / ARCHIVED
    private String status;
}
