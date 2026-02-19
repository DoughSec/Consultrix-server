package com.consultrix.consultrixserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "grades")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "submission_id", nullable = false)
    private Submission submission;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instructor_user_id", nullable = false)
    private Instructor instructor;

    @Column(name = "score", nullable = false)
    private BigDecimal score;

    @Lob
    @Column(name = "feedback")
    private String feedback;

    @Column(name = "graded_at", nullable = false)
    private LocalDateTime gradedAt = LocalDateTime.now();
}