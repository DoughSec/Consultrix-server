package com.consultrix.consultrixserver.model.dto.messageDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MessageResponseDto {
    private Integer id;
    private Integer conversationId;
    private UserSummaryDto sender;
    private String content;
    private boolean deleted;
    private LocalDateTime sentAt;
}
