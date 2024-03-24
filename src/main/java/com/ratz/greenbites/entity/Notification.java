package com.ratz.greenbites.entity;

import com.ratz.greenbites.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "notifications")
public class Notification {

    @Column(nullable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User recipient;
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    @Column(nullable = false)
    private boolean isRead = false;
    private String content;
}
