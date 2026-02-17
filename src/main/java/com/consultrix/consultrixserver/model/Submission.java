package com.consultrix.consultrixserver.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "submissions",
        uniqueConstraints = @UniqueConstraint(
        name = "uk_submission_assignment_student",
        columnNames = {"assignment_id", "student_user_id"}))
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_user_id", nullable = false)
    private Student student;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "content_url")
    private String contentUrl;

    // SUBMITTED / LATE / MISSING
    @Column(name = "status", nullable = false)
    private String status = "SUBMITTED";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
