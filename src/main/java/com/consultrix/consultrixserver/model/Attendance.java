package com.consultrix.consultrixserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@Table(name = "attendance")
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "cohort_id")
    private int cohortId;

    @Column(name = "student_user_id")
    private int studentUserId;

    @Column(name = "attendance_date")
    private LocalDate attendanceDate;

    @Column(name = "status")
    private String status;

    @Column(name = "note")
    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}

