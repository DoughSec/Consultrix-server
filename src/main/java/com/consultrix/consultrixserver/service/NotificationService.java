package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Notification;
import com.consultrix.consultrixserver.model.User;
import com.consultrix.consultrixserver.model.dto.notificationDTO.NotificationRequestDto;
import com.consultrix.consultrixserver.model.dto.notificationDTO.NotificationResponseDto;
import com.consultrix.consultrixserver.repository.NotificationRepository;
import com.consultrix.consultrixserver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    // create Notification
    public NotificationResponseDto create(Integer userId, String title, String message) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRead(false);

        notificationRepository.save(notification);

        NotificationResponseDto responseDto = new NotificationResponseDto();
        responseDto.setNotificationId(notification.getId());
        responseDto.setUserId(notification.getUser().getId());
        responseDto.setTitle(notification.getTitle());
        responseDto.setMessage(notification.getMessage());
        responseDto.setRead(notification.isRead());

        return responseDto;
    }

    // getAll
    @Transactional(readOnly = true)
    public List<Notification> listAll() {
        return notificationRepository.findAll();
    }

    // getByUser
    @Transactional(readOnly = true)
    public List<Notification> listByUser(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // getUnreadByUser
    @Transactional(readOnly = true)
    public List<Notification> listUnreadByUser(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }
        return notificationRepository.findByUserIdAndIsReadOrderByCreatedAtDesc(userId, false);
    }

    // getById
    @Transactional(readOnly = true)
    public Notification getById(Integer notificationId) {
        if (notificationId == null) {
            throw new IllegalArgumentException("notificationId is required");
        }
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + notificationId));
    }

    // mark Notification as read
    public NotificationResponseDto markAsRead(Integer notificationId) {
        Notification existing = getById(notificationId);

        existing.setRead(true);
        notificationRepository.save(existing);

        NotificationResponseDto responseDto = new NotificationResponseDto();
        responseDto.setNotificationId(existing.getId());
        responseDto.setUserId(existing.getUser().getId());
        responseDto.setTitle(existing.getTitle());
        responseDto.setMessage(existing.getMessage());
        responseDto.setRead(existing.isRead());

        return responseDto;
    }

    // update Notification
    public NotificationResponseDto update(Integer notificationId, NotificationRequestDto updated) {
        Notification existing = getById(notificationId);

        existing.setTitle(updated.getTitle());
        existing.setMessage(updated.getMessage());
        existing.setRead(updated.isRead());

        notificationRepository.save(existing);

        NotificationResponseDto responseDto = new NotificationResponseDto();
        responseDto.setNotificationId(existing.getId());
        responseDto.setUserId(existing.getUser().getId());
        responseDto.setTitle(existing.getTitle());
        responseDto.setMessage(existing.getMessage());
        responseDto.setRead(existing.isRead());

        return responseDto;
    }

    // delete Notification
    public void delete(Integer notificationId) {
        if (notificationId == null) {
            throw new IllegalArgumentException("notificationId is required");
        }
        if (!notificationRepository.existsById(notificationId)) {
            throw new IllegalArgumentException("Notification not found: " + notificationId);
        }
        notificationRepository.deleteById(notificationId);
    }
}
