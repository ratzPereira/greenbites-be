package com.ratz.greenbites.controller;

import com.ratz.greenbites.DTO.report.ReportDTO;
import com.ratz.greenbites.DTO.report.ReportResponseDTO;
import com.ratz.greenbites.DTO.report.UpdateReportStatusDTO;
import com.ratz.greenbites.entity.Report;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.enums.ReferenceType;
import com.ratz.greenbites.enums.ReportStatus;
import com.ratz.greenbites.mapper.ReportMapper;
import com.ratz.greenbites.response.HttpResponse;
import com.ratz.greenbites.services.ReportService;
import com.ratz.greenbites.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "Reports", description = "The Reports API for managing user reports.")
public class ReportController {

    private final UserService userService;
    private final ReportService reportService;
    private final ReportMapper reportMapper;


    @GetMapping("/type/{type}")
    @Operation(summary = "Get reports by type", description = "Fetches paginated reports filtered by the specified type. Requires ROLE_MANAGER, ROLE_ADMIN, or ROLE_SYSADMIN.")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_SYSADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reports fetched successfully by type",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportResponseDTO.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Reports not found")})
    public ResponseEntity<HttpResponse> getReportsByType(@PathVariable ReferenceType type, Pageable pageable) {

        Page<Report> reports = reportService.getReportsByType(type, pageable);
        Page<ReportResponseDTO> reportDTOs = reports.map(reportMapper::reportToReportResponseDTO);
        return buildPageReportResponse(reportDTOs, "Reports fetched successfully by type: " + type);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get reports by status", description = "Fetches paginated reports filtered by the specified status. Requires ROLE_MANAGER, ROLE_ADMIN, or ROLE_SYSADMIN.")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_SYSADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reports fetched successfully by status",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportResponseDTO.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Reports not found")})
    public ResponseEntity<HttpResponse> getReportsByStatus(@PathVariable ReportStatus status, Pageable pageable) {
        Page<Report> reports = reportService.getReportsByStatus(status, pageable);
        Page<ReportResponseDTO> reportDTOs = reports.map(reportMapper::reportToReportResponseDTO);
        return buildPageReportResponse(reportDTOs, "Reports fetched successfully by status: " + status);
    }

    @GetMapping("/reporter/{reporterId}")
    @Operation(summary = "Get reports by reporter ID", description = "Fetches paginated reports made by the specified reporter. Requires ROLE_MANAGER, ROLE_ADMIN, or ROLE_SYSADMIN.")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_SYSADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reports fetched successfully for reporter",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportResponseDTO.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Reports not found")})
    public ResponseEntity<HttpResponse> getReportsByReporterId(@PathVariable Long reporterId, Pageable pageable) {
        Page<Report> reports = reportService.getReportsByReporterId(reporterId, pageable);
        Page<ReportResponseDTO> reportDTOs = reports.map(reportMapper::reportToReportResponseDTO);
        return buildPageReportResponse(reportDTOs, "Reports fetched successfully for reporterId: " + reporterId);
    }

    @GetMapping("/reportedUser/{reportedUserId}")
    @Operation(summary = "Get reports by reported user ID", description = "Fetches paginated reports about the specified reported user. Requires ROLE_MANAGER, ROLE_ADMIN, or ROLE_SYSADMIN.")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_SYSADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reports fetched successfully for reported user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportResponseDTO.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Reports not found")})
    public ResponseEntity<HttpResponse> getReportsByReportedUserId(@PathVariable Long reportedUserId, Pageable pageable) {
        Page<Report> reports = reportService.getReportsByReportedUserId(reportedUserId, pageable);
        Page<ReportResponseDTO> reportDTOs = reports.map(reportMapper::reportToReportResponseDTO);
        return buildPageReportResponse(reportDTOs, "Reports fetched successfully for reportedUserId: " + reportedUserId);
    }


    @PatchMapping("/{reportId}/status")
    @Operation(summary = "Update report status", description = "Updates the status of a report. Requires ROLE_ADMIN.")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report status updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportResponseDTO.class))}),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Report not found")})
    public ResponseEntity<HttpResponse> updateReportStatus(@PathVariable Long reportId, @RequestBody UpdateReportStatusDTO statusDTO) {
        Report updatedReport = reportService.updateReportStatus(reportId, statusDTO.getStatus());
        ReportResponseDTO reportResponseDTO = reportMapper.reportToReportResponseDTO(updatedReport);
        return buildReportResponse(reportResponseDTO);
    }


    @PostMapping
    @Operation(summary = "Create a new report", description = "Creates a new report with details provided by the user. Accessible by authenticated users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Report created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReportResponseDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request")})
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
