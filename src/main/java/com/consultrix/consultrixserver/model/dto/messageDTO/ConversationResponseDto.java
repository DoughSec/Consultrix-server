package com.consultrix.consultrixserver.model.dto.messageDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ConversationResponseDto {
    private Integer id;
    private String name;
    private String type;
    private List<UserSummaryDto> members;
    private MessageResponseDto lastMessage;
    private LocalDateTime createdAt;
}
