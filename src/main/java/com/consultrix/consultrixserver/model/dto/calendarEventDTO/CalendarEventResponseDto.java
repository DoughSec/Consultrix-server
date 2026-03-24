package com.consultrix.consultrixserver.model.dto.calendarEventDTO;

import com.consultrix.consultrixserver.model.dto.messageDTO.ConversationResponseDto;
import com.consultrix.consultrixserver.model.dto.messageDTO.UserSummaryDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CalendarEventResponseDto {
    private Integer id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String eventType;
    private Integer cohortId;
    private String cohortName;
    private ConversationResponseDto conversation;
    private UserSummaryDto createdBy;
    private LocalDateTime createdAt;
}
