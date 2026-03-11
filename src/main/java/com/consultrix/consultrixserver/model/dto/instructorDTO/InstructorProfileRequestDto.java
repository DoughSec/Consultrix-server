package com.consultrix.consultrixserver.model.dto.instructorDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstructorProfileRequestDto {
    private Integer userId;
    private String firstName;
    private String lastName;
    private String email;
    private String status;
    private String title;
    private String specialty;
    private String officeHours;
}
