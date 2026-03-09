package com.consultrix.consultrixserver.model.dto.adminDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminResponseDto {
    private Integer adminId;
    private String firstName;
    private String lastName;
    private String email;
    private String status;
}
