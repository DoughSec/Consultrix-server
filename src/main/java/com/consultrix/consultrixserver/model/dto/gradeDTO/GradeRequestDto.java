package com.consultrix.consultrixserver.model.dto.gradeDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GradeRequestDto {
    private Integer submissionId;
    private Integer instructorUserId;
    private BigDecimal score;
    private String feedback;
}
