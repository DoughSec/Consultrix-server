package com.consultrix.consultrixserver.model.dto.facilityDTO;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FacilityRequestDto {
    private Integer organizationId;
    private String name;
    private String address_line1;
    private String city;
    private String state;
    private String country;
    private Integer capacity = 0;
    private LocalDate leaseStartDate;
    private LocalDate leaseEndDate;
    // ACTIVE / PLANNED / CLOSED
    private String status;
}
