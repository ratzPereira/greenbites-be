package com.ratz.greenbites.DTO.report;

import com.ratz.greenbites.enums.ReportStatus;
import lombok.Data;

@Data
public class UpdateReportStatusDTO {
    private ReportStatus status;
}