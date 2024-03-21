package com.ratz.greenbites.services.Impl;

import com.ratz.greenbites.DTO.report.ReportDTO;
import com.ratz.greenbites.entity.Report;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.enums.ReferenceType;
import com.ratz.greenbites.enums.ReportStatus;
import com.ratz.greenbites.repository.ReportRepository;
import com.ratz.greenbites.repository.UserRepository;
import com.ratz.greenbites.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    @Override
    public Report createReport(ReportDTO reportDTO, User user) {

        User reportedUser = userRepository.findById(reportDTO.getReportedUserId())
                .orElseThrow(() -> new RuntimeException("Reported user not found"));

        Report report = new Report();
        report.setReporter(user);
        report.setReferenceId(reportDTO.getReferenceId());
        report.setReferenceType(reportDTO.getReferenceType());
        report.setReason(reportDTO.getReason());
        report.setStatus(ReportStatus.PENDING);
        report.setReportedAt(LocalDateTime.now());
        report.setReportedUser(reportedUser);
        return reportRepository.save(report);
    }

    @Override
    public Page<Report> getReportsByType(ReferenceType type, Pageable pageable) {
        return reportRepository.findByReferenceType(type, pageable);
    }

    @Override
    public Page<Report> getReportsByStatus(ReportStatus status, Pageable pageable) {
        return reportRepository.findByStatus(status, pageable);
    }

    @Override
    public Page<Report> getReportsByReporterId(Long reporterId, Pageable pageable) {
        return reportRepository.findByReporterId(reporterId, pageable);
    }

    @Override
    public Page<Report> getReportsByReportedUserId(Long reportedUserId, Pageable pageable) {
        return reportRepository.findByReportedUserId(reportedUserId, pageable);
    }
}
