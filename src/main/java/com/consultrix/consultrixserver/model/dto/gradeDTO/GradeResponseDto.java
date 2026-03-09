package com.consultrix.consultrixserver.model.dto.gradeDTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GradeResponseDto {
    private Integer gradeId;
    private Integer submissionId;
    private Integer instructorUserId;
    private BigDecimal score;
    private String feedback;
}
