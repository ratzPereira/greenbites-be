package com.ratz.greenbites.repository;

import com.ratz.greenbites.entity.Report;
import com.ratz.greenbites.enums.ReferenceType;
import com.ratz.greenbites.enums.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Page<Report> findByReferenceType(ReferenceType referenceType, Pageable pageable);

    Page<Report> findByStatus(ReportStatus status, Pageable pageable);

    Page<Report> findByReporterId(Long reporterId, Pageable pageable);

    Page<Report> findByReportedUserId(Long reportedUserId, Pageable pageable);
}
