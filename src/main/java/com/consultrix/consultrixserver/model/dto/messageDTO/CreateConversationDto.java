package com.consultrix.consultrixserver.model.dto.messageDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateConversationDto {
    private String name;
    private String type; // DIRECT | GROUP
    private List<Integer> memberIds;
}
