package com.consultrix.consultrixserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "planned_student_events",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_user_id", "event_date"}))
public class PlannedStudentEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "student_user_id", nullable = false)
    private Student student;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    /** LATE | REMOTE | OFF */
    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "note")
    private String note;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
