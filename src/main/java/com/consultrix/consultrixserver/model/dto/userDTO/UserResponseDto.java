package com.consultrix.consultrixserver.model.dto.userDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private Integer userId;
    private Integer organizationId;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    // ACTIVE // INACTIVE // SUSPENDED
    private String status;
}
