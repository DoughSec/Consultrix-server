package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.dto.messageDTO.MessageResponseDto;
import com.consultrix.consultrixserver.model.dto.messageDTO.SendMessageDto;
import com.consultrix.consultrixserver.service.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class MessageWebSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    public MessageWebSocketController(MessageService messageService,
                                      SimpMessagingTemplate messagingTemplate) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload SendMessageDto dto, Principal principal) {
        if (principal == null) return;

        Integer senderId = Integer.parseInt(principal.getName());
        MessageResponseDto message = messageService.sendMessage(
                senderId,
                dto.getConversationId(),
                dto.getContent()
        );

        // Broadcast to all members subscribed to this conversation topic
        messagingTemplate.convertAndSend(
                "/topic/conversation." + dto.getConversationId(),
                message
        );
    }
}
