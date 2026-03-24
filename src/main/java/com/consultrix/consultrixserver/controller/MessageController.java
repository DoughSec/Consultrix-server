package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.dto.messageDTO.ConversationResponseDto;
import com.consultrix.consultrixserver.model.dto.messageDTO.CreateConversationDto;
import com.consultrix.consultrixserver.model.dto.messageDTO.MessageResponseDto;
import com.consultrix.consultrixserver.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/conversations")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<ConversationResponseDto> getMyConversations(Authentication auth) {
        Integer userId = Integer.parseInt(auth.getName());
        return messageService.getConversationsForUser(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public ConversationResponseDto createConversation(@RequestBody CreateConversationDto dto,
                                                      Authentication auth) {
        Integer userId = Integer.parseInt(auth.getName());
        return messageService.createConversation(userId, dto);
    }

    @GetMapping("/{conversationId}/messages")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<MessageResponseDto> getMessages(@PathVariable Integer conversationId,
                                                Authentication auth) {
        Integer userId = Integer.parseInt(auth.getName());
        return messageService.getMessages(conversationId, userId);
    }

    @DeleteMapping("/messages/{messageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public void deleteMessage(@PathVariable Integer messageId, Authentication auth) {
        Integer userId = Integer.parseInt(auth.getName());
        messageService.deleteMessage(messageId, userId);
    }
}
