package com.ratz.greenbites.mapper;

import com.ratz.greenbites.DTO.report.ReportResponseDTO;
import com.ratz.greenbites.entity.Report;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-21T00:26:07-0100",
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
}
