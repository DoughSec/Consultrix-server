package com.consultrix.consultrixserver.model.dto.flagDTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentFlagResponseDto {

    private Integer id;

    private Integer studentId;
    private String studentFirstName;
    private String studentLastName;

    private Integer instructorId;
    private String instructorFirstName;
    private String instructorLastName;

    private String reason;

    private String priority;

    private boolean resolved;

    private LocalDateTime resolvedAt;

    private LocalDateTime createdAt;

}
