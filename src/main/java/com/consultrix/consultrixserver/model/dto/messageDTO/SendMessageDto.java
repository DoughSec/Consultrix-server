package com.consultrix.consultrixserver.model.dto.messageDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessageDto {
    private Integer conversationId;
    private String content;
}
