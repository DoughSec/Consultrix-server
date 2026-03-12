package com.consultrix.consultrixserver.model.dto.userDTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {
    private Integer organizationId;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    // ACTIVE // INACTIVE // SUSPENDED
    private String status;
}
