package com.consultrix.consultrixserver.repository;

import com.consultrix.consultrixserver.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(int userId);
    List<Notification> findByUserIdAndIsReadOrderByCreatedAtDesc(int userId, boolean isRead);
}
