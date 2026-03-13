package com.consultrix.consultrixserver.model.dto.studentDTO;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentRequestDto {
    private Integer cohortId;
    private Integer organizationId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String status;
    // ACTIVE / COMPLETED / WITHDRAWN
    private String graduationStatus;
    // NOT_STARTED / IN_PROGRESS / COMPLETED
    private String pipelineStage;
    // NONE / SCREEN / TECHNICAL / FINAL
    private String interviewStage;
    private String clientName;
    private LocalDate placementStartDate;
    private String resumeUrl;
    private String notes;
}
