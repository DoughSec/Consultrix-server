package com.consultrix.consultrixserver.model.dto.ticketDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TicketResponseDto {
    private Integer ticketId;
    private Integer submittedByUserId;
    private String submittedByName;
    private String issue;
    private String status;
    private LocalDateTime createdAt;
}

