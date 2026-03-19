package com.consultrix.consultrixserver.model.dto.studentDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentProfileResponseDto {
    private Integer userId;
    private Integer cohortId;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}
