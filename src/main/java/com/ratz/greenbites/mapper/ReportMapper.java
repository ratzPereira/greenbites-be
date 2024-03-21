package com.ratz.greenbites.mapper;

import com.ratz.greenbites.DTO.report.ReportResponseDTO;
import com.ratz.greenbites.entity.Report;
import com.ratz.greenbites.enums.ReferenceType;
import com.ratz.greenbites.enums.ReportStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    ReportMapper INSTANCE = Mappers.getMapper(ReportMapper.class);

    @Named("referenceTypeToString")
    static String referenceTypeToString(ReferenceType referenceType) {
        return referenceType.toString();
    }

    @Named("reportStatusToString")
    static String reportStatusToString(ReportStatus status) {
        return status.toString();
    }

    ReportResponseDTO reportToReportResponseDTO(Report report);
}
