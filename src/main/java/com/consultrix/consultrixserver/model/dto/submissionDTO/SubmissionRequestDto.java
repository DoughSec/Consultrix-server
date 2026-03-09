package com.consultrix.consultrixserver.model.dto.submissionDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SubmissionRequestDto {
    private LocalDateTime submittedAt;
    private String contentUrl;
    // SUBMITTED / LATE / MISSING
    private String status;
}
