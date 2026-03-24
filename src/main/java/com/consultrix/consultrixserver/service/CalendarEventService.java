package com.consultrix.consultrixserver.service;

import com.consultrix.consultrixserver.model.CalendarEvent;
import com.consultrix.consultrixserver.model.Cohort;
import com.consultrix.consultrixserver.model.Conversation;
import com.consultrix.consultrixserver.model.Student;
import com.consultrix.consultrixserver.model.User;
import com.consultrix.consultrixserver.model.dto.calendarEventDTO.CalendarEventRequestDto;
import com.consultrix.consultrixserver.model.dto.calendarEventDTO.CalendarEventResponseDto;
import com.consultrix.consultrixserver.model.dto.messageDTO.ConversationResponseDto;
import com.consultrix.consultrixserver.model.dto.messageDTO.UserSummaryDto;
import com.consultrix.consultrixserver.repository.CalendarEventRepository;
import com.consultrix.consultrixserver.repository.CohortRepository;
import com.consultrix.consultrixserver.repository.ConversationRepository;
import com.consultrix.consultrixserver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CalendarEventService {

    private final CalendarEventRepository calendarEventRepository;
    private final UserRepository userRepository;
    private final CohortRepository cohortRepository;
    private final ConversationRepository conversationRepository;
    private final MessageService messageService;

    public CalendarEventService(CalendarEventRepository calendarEventRepository,
                                UserRepository userRepository,
                                CohortRepository cohortRepository,
                                ConversationRepository conversationRepository,
                                MessageService messageService) {
        this.calendarEventRepository = calendarEventRepository;
        this.userRepository = userRepository;
        this.cohortRepository = cohortRepository;
        this.conversationRepository = conversationRepository;
        this.messageService = messageService;
    }

    @Transactional(readOnly = true)
    public List<CalendarEventResponseDto> getEventsForUser(Integer userId) {
        User user = getUser(userId);
        if (user instanceof Student student && student.getCohort() != null) {
            return calendarEventRepository
                    .findByCohortIdOrGlobal(student.getCohort().getId())
                    .stream()
                    .map(e -> toDto(e, userId))
                    .toList();
        }
        // instructors/admins see all events
        return calendarEventRepository.findAllByOrderByStartTimeAsc()
                .stream()
                .map(e -> toDto(e, userId))
                .toList();
    }

    public CalendarEventResponseDto createEvent(Integer creatorId, CalendarEventRequestDto req) {
        User creator = getUser(creatorId);

        CalendarEvent event = new CalendarEvent();
        event.setTitle(req.getTitle());
        event.setDescription(req.getDescription());
        event.setStartTime(req.getStartTime());
        event.setEndTime(req.getEndTime());
        event.setEventType(req.getEventType() != null ? req.getEventType() : "OTHER");
        event.setCreatedBy(creator);

        if (req.getCohortId() != null) {
            Cohort cohort = cohortRepository.findById(req.getCohortId())
                    .orElseThrow(() -> new IllegalArgumentException("Cohort not found: " + req.getCohortId()));
            event.setCohort(cohort);
        }

        if (req.getConversationId() != null) {
            Conversation conv = conversationRepository.findById(req.getConversationId())
                    .orElseThrow(() -> new IllegalArgumentException("Conversation not found: " + req.getConversationId()));
            event.setConversation(conv);
        }

        calendarEventRepository.save(event);
        return toDto(event, creatorId);
    }

    public void deleteEvent(Integer eventId, Integer requestingUserId) {
        CalendarEvent event = calendarEventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + eventId));
        User requestingUser = getUser(requestingUserId);
        boolean isCreator = event.getCreatedBy().getId().equals(requestingUserId);
        boolean isAdmin = "ROLE_ADMIN".equals(requestingUser.getRole());
        if (!isCreator && !isAdmin) {
            throw new IllegalArgumentException("Not authorized to delete this event");
        }
        calendarEventRepository.deleteById(eventId);
    }

    // ── Mapper ────────────────────────────────────────────────────────────────

    public CalendarEventResponseDto toDto(CalendarEvent e, Integer viewerId) {
        CalendarEventResponseDto dto = new CalendarEventResponseDto();
        dto.setId(e.getId());
        dto.setTitle(e.getTitle());
        dto.setDescription(e.getDescription());
        dto.setStartTime(e.getStartTime());
        dto.setEndTime(e.getEndTime());
        dto.setEventType(e.getEventType());
        dto.setCreatedAt(e.getCreatedAt());

        if (e.getCohort() != null) {
            dto.setCohortId(e.getCohort().getId());
            dto.setCohortName(e.getCohort().getName());
        }

        if (e.getConversation() != null) {
            ConversationResponseDto convDto = messageService.toConversationDto(e.getConversation(), viewerId);
            dto.setConversation(convDto);
        }

        UserSummaryDto createdByDto = new UserSummaryDto();
        createdByDto.setId(e.getCreatedBy().getId());
        createdByDto.setFirstName(e.getCreatedBy().getFirstName());
        createdByDto.setLastName(e.getCreatedBy().getLastName());
        createdByDto.setRole(e.getCreatedBy().getRole());
        dto.setCreatedBy(createdByDto);

        return dto;
    }

    private User getUser(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
    }
}
