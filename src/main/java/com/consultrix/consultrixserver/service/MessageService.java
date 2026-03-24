package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.Conversation;
import com.consultrix.consultrixserver.model.Message;
import com.consultrix.consultrixserver.model.User;
import com.consultrix.consultrixserver.model.dto.messageDTO.*;
import com.consultrix.consultrixserver.repository.ConversationRepository;
import com.consultrix.consultrixserver.repository.MessageRepository;
import com.consultrix.consultrixserver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MessageService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageService(ConversationRepository conversationRepository,
                          MessageRepository messageRepository,
                          UserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    // ── Conversations ─────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<ConversationResponseDto> getConversationsForUser(Integer userId) {
        return conversationRepository.findByMemberId(userId)
                .stream()
                .map(c -> toConversationDto(c, userId))
                .toList();
    }

    public ConversationResponseDto createConversation(Integer creatorId, CreateConversationDto dto) {
        User creator = getUser(creatorId);

        // For DM: reuse existing if present
        if ("DIRECT".equals(dto.getType()) && dto.getMemberIds() != null && dto.getMemberIds().size() == 1) {
            Integer otherId = dto.getMemberIds().get(0);
            var existing = conversationRepository.findDirectConversation(creatorId, otherId);
            if (existing.isPresent()) {
                return toConversationDto(existing.get(), creatorId);
            }
        }

        Conversation conversation = new Conversation();
        conversation.setName(dto.getName());
        conversation.setType(dto.getType() != null ? dto.getType() : "DIRECT");
        conversation.setCreatedBy(creator);

        Set<User> members = new HashSet<>();
        members.add(creator);
        if (dto.getMemberIds() != null) {
            dto.getMemberIds().forEach(id -> members.add(getUser(id)));
        }
        conversation.setMembers(members);

        conversationRepository.save(conversation);
        return toConversationDto(conversation, creatorId);
    }

    // ── Messages ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<MessageResponseDto> getMessages(Integer conversationId, Integer requestingUserId) {
        Conversation conversation = getConversation(conversationId);
        boolean isMember = conversation.getMembers().stream()
                .anyMatch(m -> m.getId().equals(requestingUserId));
        if (!isMember) {
            throw new IllegalArgumentException("User is not a member of this conversation");
        }
        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId)
                .stream()
                .map(this::toMessageDto)
                .toList();
    }

    public MessageResponseDto sendMessage(Integer senderId, Integer conversationId, String content) {
        Conversation conversation = getConversation(conversationId);
        User sender = getUser(senderId);

        boolean isMember = conversation.getMembers().stream()
                .anyMatch(m -> m.getId().equals(senderId));
        if (!isMember) {
            throw new IllegalArgumentException("User is not a member of this conversation");
        }

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(content);
        messageRepository.save(message);

        return toMessageDto(message);
    }

    public void deleteConversation(Integer conversationId, Integer requestingUserId) {
        Conversation conversation = getConversation(conversationId);
        boolean isMember = conversation.getMembers().stream()
                .anyMatch(m -> m.getId().equals(requestingUserId));
        if (!isMember) {
            throw new IllegalArgumentException("User is not a member of this conversation");
        }
        conversationRepository.delete(conversation);
    }

    public void deleteMessage(Integer messageId, Integer requestingUserId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found: " + messageId));
        if (!message.getSender().getId().equals(requestingUserId)) {
            throw new IllegalArgumentException("Not authorized to delete this message");
        }
        message.setDeleted(true);
        messageRepository.save(message);
    }

    // ── Mappers ───────────────────────────────────────────────────────────────

    public ConversationResponseDto toConversationDto(Conversation c, Integer viewerId) {
        ConversationResponseDto dto = new ConversationResponseDto();
        dto.setId(c.getId());
        dto.setType(c.getType());
        dto.setCreatedAt(c.getCreatedAt());

        // For DMs, name is the other person's name
        if ("DIRECT".equals(c.getType())) {
            c.getMembers().stream()
                    .filter(m -> !m.getId().equals(viewerId))
                    .findFirst()
                    .ifPresent(other -> dto.setName(other.getFirstName() + " " + other.getLastName()));
        } else {
            dto.setName(c.getName());
        }

        dto.setMembers(c.getMembers().stream().map(this::toUserSummary).toList());

        // last message
        List<Message> messages = messageRepository.findByConversationIdOrderBySentAtAsc(c.getId());
        if (!messages.isEmpty()) {
            dto.setLastMessage(toMessageDto(messages.get(messages.size() - 1)));
        }

        return dto;
    }

    public MessageResponseDto toMessageDto(Message m) {
        MessageResponseDto dto = new MessageResponseDto();
        dto.setId(m.getId());
        dto.setConversationId(m.getConversation().getId());
        dto.setSender(toUserSummary(m.getSender()));
        dto.setContent(m.isDeleted() ? "[deleted]" : m.getContent());
        dto.setDeleted(m.isDeleted());
        dto.setSentAt(m.getSentAt());
        return dto;
    }

    private UserSummaryDto toUserSummary(User u) {
        UserSummaryDto dto = new UserSummaryDto();
        dto.setId(u.getId());
        dto.setFirstName(u.getFirstName());
        dto.setLastName(u.getLastName());
        dto.setRole(u.getRole());
        return dto;
    }

    private User getUser(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }

    private Conversation getConversation(Integer id) {
        return conversationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found: " + id));
    }
}
