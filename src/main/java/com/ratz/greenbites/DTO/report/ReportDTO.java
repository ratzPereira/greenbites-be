package com.ratz.greenbites.DTO.report;

import com.ratz.greenbites.enums.ReferenceType;
import lombok.Data;

@Data
public class ReportDTO {

    private Long id;
    private Long referenceId;
    private ReferenceType referenceType;
    private String reason;
    private Long reportedUserId;
}
