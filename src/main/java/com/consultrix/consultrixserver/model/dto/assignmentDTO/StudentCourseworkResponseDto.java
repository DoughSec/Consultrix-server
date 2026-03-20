package com.consultrix.consultrixserver.model.dto.assignmentDTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class StudentCourseworkResponseDto {
    private Integer assignmentId;
    private Integer moduleId;
    private String moduleTitle;
    private Integer cohortId;
    private String cohortName;
    private String title;
    private String description;
    private LocalDate dueDate;
    private LocalDateTime dueTime;
    private BigDecimal maxScore;
    private Integer submissionId;
    private LocalDateTime submittedAt;
    private String submissionStatus;
    private Integer gradeId;
    private BigDecimal score;
    private BigDecimal assignmentGradePercentage;
    private String feedback;
    private String courseworkStatus;
}
