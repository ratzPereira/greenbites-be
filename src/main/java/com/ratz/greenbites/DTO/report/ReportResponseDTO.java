package com.ratz.greenbites.DTO.report;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportResponseDTO {

    private Long reportId;
    private String referenceType;
    private Long referenceId;
    private String status;
    private String reason;
    private LocalDateTime reportedAt;
    private Long reportedUserId;
}

