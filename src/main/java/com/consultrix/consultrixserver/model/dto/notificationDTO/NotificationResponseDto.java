package com.consultrix.consultrixserver.model.dto.notificationDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationResponseDto {
    private Integer notificationId;
    private Integer userId;
    private String title;
    private String message;
    private boolean isRead;
}
