package com.consultrix.consultrixserver.model.dto.notificationDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequestDto {
    private String title;
    private String message;
    private boolean isRead;
}
