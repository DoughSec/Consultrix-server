package com.consultrix.consultrixserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@Table(name = "cohorts")
public class Cohort {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "warehouse_id")
    private int warehouseId;

    @Column(name = "primary_instructor_user_id")
    private int primaryInstructorUserId;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
