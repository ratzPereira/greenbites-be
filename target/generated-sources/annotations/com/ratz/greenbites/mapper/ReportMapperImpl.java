package com.ratz.greenbites.mapper;

import com.ratz.greenbites.DTO.report.ReportResponseDTO;
import com.ratz.greenbites.entity.Report;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.enums.ReferenceType;
import com.ratz.greenbites.enums.ReportStatus;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-21T13:07:30-0100",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 18.0.2 (Amazon.com Inc.)"
)
@Component
public class ReportMapperImpl implements ReportMapper {

    @Override
    public ReportResponseDTO reportToReportResponseDTO(Report report) {
        if ( report == null ) {
            return null;
        }

        ReportResponseDTO reportResponseDTO = new ReportResponseDTO();

        reportResponseDTO.setReportedUserId( reportReportedUserId( report ) );
        if ( report.getReferenceType() != null ) {
            reportResponseDTO.setReferenceType( report.getReferenceType().name() );
        }
        reportResponseDTO.setReferenceId( report.getReferenceId() );
        if ( report.getStatus() != null ) {
            reportResponseDTO.setStatus( report.getStatus().name() );
        }
        reportResponseDTO.setReason( report.getReason() );
        reportResponseDTO.setReportedAt( report.getReportedAt() );

        return reportResponseDTO;
    }

    @Override
    public Report reportResponseDTOToReport(ReportResponseDTO reportResponseDTO) {
        if ( reportResponseDTO == null ) {
            return null;
        }

        Report report = new Report();

        report.setReferenceId( reportResponseDTO.getReferenceId() );
        if ( reportResponseDTO.getReferenceType() != null ) {
            report.setReferenceType( Enum.valueOf( ReferenceType.class, reportResponseDTO.getReferenceType() ) );
        }
        report.setReason( reportResponseDTO.getReason() );
        if ( reportResponseDTO.getStatus() != null ) {
            report.setStatus( Enum.valueOf( ReportStatus.class, reportResponseDTO.getStatus() ) );
        }
        report.setReportedAt( reportResponseDTO.getReportedAt() );

        return report;
    }

    private Long reportReportedUserId(Report report) {
        if ( report == null ) {
            return null;
        }
        User reportedUser = report.getReportedUser();
        if ( reportedUser == null ) {
            return null;
        }
        Long id = reportedUser.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
