package com.consultrix.consultrixserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Column(name = "is_read")
    private boolean isRead;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
