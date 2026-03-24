package com.consultrix.consultrixserver.model.dto.flagDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentFlagRequestDto {

    private Integer studentId;

    private String reason;

    // LOW / MEDIUM / HIGH
    private String priority;

}
