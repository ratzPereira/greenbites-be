package com.ratz.greenbites.controller;

import com.ratz.greenbites.DTO.report.ReportDTO;
import com.ratz.greenbites.DTO.report.ReportResponseDTO;
import com.ratz.greenbites.entity.Report;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.enums.ReferenceType;
import com.ratz.greenbites.mapper.ReportMapper;
import com.ratz.greenbites.response.HttpResponse;
import com.ratz.greenbites.services.ReportService;
import com.ratz.greenbites.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reports")
@Slf4j
public class ReportController {

    private final UserService userService;
    private final ReportService reportService;
    private final ReportMapper reportMapper;


    @GetMapping("/type/{type}")
    public ResponseEntity<HttpResponse> getReportsByType(@PathVariable ReferenceType type, Pageable pageable) {

        Page<Report> reports = reportService.getReportsByType(type, pageable);
        Page<ReportResponseDTO> reportDTOs = reports.map(reportMapper::reportToReportResponseDTO);
        return buildPageReportResponse(reportDTOs, "Reports fetched successfully by type: " + type);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<HttpResponse> getReportsByStatus(@PathVariable ReportStatus status, Pageable pageable) {
        Page<Report> reports = reportService.getReportsByStatus(status, pageable);
        Page<ReportResponseDTO> reportDTOs = reports.map(reportMapper::reportToReportResponseDTO);
        return buildPageReportResponse(reportDTOs, "Reports fetched successfully by status: " + status);
    }

    @GetMapping("/reporter/{reporterId}")
    public ResponseEntity<HttpResponse> getReportsByReporterId(@PathVariable Long reporterId, Pageable pageable) {
        Page<Report> reports = reportService.getReportsByReporterId(reporterId, pageable);
        Page<ReportResponseDTO> reportDTOs = reports.map(reportMapper::reportToReportResponseDTO);
        return buildPageReportResponse(reportDTOs, "Reports fetched successfully for reporterId: " + reporterId);
    }

    @GetMapping("/reportedUser/{reportedUserId}")
    public ResponseEntity<HttpResponse> getReportsByReportedUserId(@PathVariable Long reportedUserId, Pageable pageable) {
        Page<Report> reports = reportService.getReportsByReportedUserId(reportedUserId, pageable);
        Page<ReportResponseDTO> reportDTOs = reports.map(reportMapper::reportToReportResponseDTO);
        return buildPageReportResponse(reportDTOs, "Reports fetched successfully for reportedUserId: " + reportedUserId);
    }

    @PostMapping
    public ResponseEntity<HttpResponse> createReport(@RequestBody ReportDTO reportDTO) {

        log.info("Attempting to create a new report for reference type: {} and reference id: {}",
                reportDTO.getReferenceType(), reportDTO.getReferenceId());

        User user = getAuthenticatedUser();
        Report report = reportService.createReport(reportDTO, user);
        ReportResponseDTO reportResponseDTO = ReportMapper.INSTANCE.reportToReportResponseDTO(report);
        return buildReportResponse(reportResponseDTO);
    }

    public ResponseEntity<HttpResponse> buildReportResponse(ReportResponseDTO reportDTO) {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("report", reportDTO);

        return ResponseEntity.ok()
                .body(HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(responseData)
                        .message("Report processed successfully")
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .build());
    }

    private ResponseEntity<HttpResponse> buildPageReportResponse(Page<ReportResponseDTO> reportResponseDTOs, String message) {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("reports", reportResponseDTOs.getContent());

        HttpResponse response = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .data(responseData)
                .message(message)
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .build();

        return ResponseEntity.ok(response);
    }


    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userService.getUserByEmail(currentUsername);
    }
}
