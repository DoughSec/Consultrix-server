package com.consultrix.consultrixserver.model.dto.ticketDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequestDto {
    private Integer submittedByUserId;
    private String issue;
}

