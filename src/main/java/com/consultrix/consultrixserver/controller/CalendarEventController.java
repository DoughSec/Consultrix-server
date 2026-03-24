package com.consultrix.consultrixserver.controller;

import com.consultrix.consultrixserver.model.dto.calendarEventDTO.CalendarEventRequestDto;
import com.consultrix.consultrixserver.model.dto.calendarEventDTO.CalendarEventResponseDto;
import com.consultrix.consultrixserver.service.CalendarEventService;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultrix/calendar/events")
public class CalendarEventController {

    private final CalendarEventService calendarEventService;
    private final SimpMessagingTemplate messagingTemplate;

    public CalendarEventController(CalendarEventService calendarEventService,
                                   SimpMessagingTemplate messagingTemplate) {
        this.calendarEventService = calendarEventService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_STUDENT','ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public List<CalendarEventResponseDto> getMyEvents(Authentication auth) {
        Integer userId = Integer.parseInt(auth.getName());
        return calendarEventService.getEventsForUser(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public CalendarEventResponseDto createEvent(@RequestBody CalendarEventRequestDto dto,
                                                Authentication auth) {
        Integer userId = Integer.parseInt(auth.getName());
        CalendarEventResponseDto created = calendarEventService.createEvent(userId, dto);

        // Broadcast to the relevant cohort topic (or global)
        String topic = created.getCohortId() != null
                ? "/topic/calendar.cohort." + created.getCohortId()
                : "/topic/calendar.all";
        messagingTemplate.convertAndSend(topic, created);

        return created;
    }

    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ROLE_INSTRUCTOR','ROLE_ADMIN')")
    public void deleteEvent(@PathVariable Integer eventId, Authentication auth) {
        Integer userId = Integer.parseInt(auth.getName());
        calendarEventService.deleteEvent(eventId, userId);
    }
}
