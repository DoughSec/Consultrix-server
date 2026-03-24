package com.consultrix.consultrixserver.model.dto.messageDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSummaryDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String role;
}
