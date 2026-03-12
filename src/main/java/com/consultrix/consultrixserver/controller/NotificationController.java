package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.Notification;
import com.consultrix.consultrixserver.model.dto.notificationDTO.NotificationRequestDto;
import com.consultrix.consultrixserver.model.dto.notificationDTO.NotificationResponseDto;
import com.consultrix.consultrixserver.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    //create Notification record
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public NotificationResponseDto create(@RequestBody NotificationRequestDto request) {
        return notificationService.create(
                request.getUserId(),
                request.getTitle(),
                request.getMessage()
        );
    }

    //get all Notification records
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Notification> getAll() {
        return notificationService.listAll();
    }

    //get Notification by id
    @GetMapping("/{notificationId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public Notification getNotificationById(@PathVariable("notificationId") Integer notificationId) {
        return notificationService.getById(notificationId);
    }

    //get Notification records by user
    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Notification> getNotificationByUser(@PathVariable("userId") Integer userId) {
        return notificationService.listByUser(userId);
    }

    //get unread Notification records by user
    @GetMapping("/user/{userId}/unread")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<Notification> getUnreadNotificationByUser(@PathVariable("userId") Integer userId) {
        return notificationService.listUnreadByUser(userId);
    }

    //mark Notification as read
    @PatchMapping("/{notificationId}/read")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public NotificationResponseDto markAsRead(@PathVariable("notificationId") Integer notificationId) {
        return notificationService.markAsRead(notificationId);
    }

    //update Notification record
    @PutMapping("/{notificationId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public NotificationResponseDto updateNotification(@PathVariable("notificationId") Integer notificationId, @RequestBody NotificationRequestDto request) {
        return notificationService.update(notificationId, request);
    }

    //delete Notification record
    @DeleteMapping("/{notificationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteNotification(@PathVariable("notificationId") Integer notificationId) {
        notificationService.delete(notificationId);
    }

}

