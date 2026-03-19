package com.consultrix.consultrixserver.model.dto.gradeDTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GradeProfileResponseDto {
    private Integer submissionId;
    private Integer assignmentId;
    private Integer moduleId;
    private String overallLetterGrade;
    private BigDecimal overallGradePercentage;
    private BigDecimal moduleGradePercentage;
    private BigDecimal assignmentGradePercentage;
    private BigDecimal score;
    private String feedback;
}
