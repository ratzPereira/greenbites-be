package com.ratz.greenbites.controller;

import com.ratz.greenbites.DTO.report.ReportDTO;
import com.ratz.greenbites.DTO.report.ReportResponseDTO;
import com.ratz.greenbites.entity.Report;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.mapper.ReportMapper;
import com.ratz.greenbites.response.HttpResponse;
import com.ratz.greenbites.services.ReportService;
import com.ratz.greenbites.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userService.getUserByEmail(currentUsername);
    }
}
