package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.report.ReportDTO;
import com.ratz.greenbites.entity.Report;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.enums.ReferenceType;
import com.ratz.greenbites.enums.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {

    Report createReport(ReportDTO reportDTO, User user);

    Page<Report> getReportsByType(ReferenceType type, Pageable pageable);

    Page<Report> getReportsByStatus(ReportStatus status, Pageable pageable);

    Page<Report> getReportsByReporterId(Long reporterId, Pageable pageable);

    Page<Report> getReportsByReportedUserId(Long reportedUserId, Pageable pageable);

}
