package com.consultrix.consultrixserver.model.dto.organizationDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationRequestDto {
    private String name;
    // ACCENTURE / PERSCHOLAS / PEOPLESHORES / CLIENT / OTHER
    private String type;
    private String contactEmail;
}
