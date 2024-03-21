package com.ratz.greenbites.services.Impl;

import com.ratz.greenbites.DTO.report.ReportDTO;
import com.ratz.greenbites.entity.Report;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.enums.ReportStatus;
import com.ratz.greenbites.repository.ReportRepository;
import com.ratz.greenbites.repository.UserRepository;
import com.ratz.greenbites.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    @Override
    public Report createReport(ReportDTO reportDTO, User user) {

        Report report = new Report();
        report.setReporter(user);
        report.setReferenceId(reportDTO.getReferenceId());
        report.setReferenceType(reportDTO.getReferenceType());
        report.setReason(reportDTO.getReason());
        report.setStatus(ReportStatus.PENDING);
        report.setReportedAt(LocalDateTime.now());
        return reportRepository.save(report);
    }
}
