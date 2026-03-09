package com.consultrix.consultrixserver.model.dto.submissionDTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SubmissionResponseDto {
    private Integer submissionId;
    private Integer assignmentId;
    private Integer studentUserId;
    private LocalDateTime submittedAt;
    private String contentUrl;
    // SUBMITTED / LATE / MISSING
    private String status;
}
