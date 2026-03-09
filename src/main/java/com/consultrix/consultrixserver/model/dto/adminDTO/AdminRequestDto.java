package com.consultrix.consultrixserver.model.dto.adminDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String status;
}
