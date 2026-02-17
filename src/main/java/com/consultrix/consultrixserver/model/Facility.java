package com.consultrix.consultrixserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@Table(name = "facilities")
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "organization_id")
    private int organizationId;

    @Column(name = "name")
    private String name;

    @Column(name = "address_line1")
    private String address_line1;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "capacity")
    private BigInteger capacity;

    @Column(name = "lease_start_date")
    private LocalDate leaseStartDate;

    @Column(name = "lease_end_date")
    private LocalDate leaseEndDate;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}