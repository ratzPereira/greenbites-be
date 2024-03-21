package com.ratz.greenbites.entity;


import com.ratz.greenbites.enums.ReferenceType;
import com.ratz.greenbites.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;

    private Long referenceId;

    @Enumerated(EnumType.STRING)
    private ReferenceType referenceType;

    private String reason;

    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.PENDING;


    @ManyToOne(fetch = FetchType.LAZY)
    private Post reportedPost;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment reportedComment;


    private LocalDateTime reportedAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}
