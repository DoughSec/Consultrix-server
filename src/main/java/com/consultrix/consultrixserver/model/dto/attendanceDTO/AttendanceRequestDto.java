package com.consultrix.consultrixserver.model.dto.attendanceDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AttendanceRequestDto {
    private LocalDate attendanceDate;
    private String status;
    private String note;
}
