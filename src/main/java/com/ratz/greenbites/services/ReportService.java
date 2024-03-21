package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.report.ReportDTO;
import com.ratz.greenbites.entity.Report;
import com.ratz.greenbites.entity.User;

public interface ReportService {

    Report createReport(ReportDTO reportDTO, User user);
}
