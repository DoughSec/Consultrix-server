package com.consultrix.consultrixserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "assignment_id")
    private int assignmentId;

    @Column(name = "student_user_id")
    private int studentUserId;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "content_url")
    private String contentUrl;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
