package com.ratz.greenbites.DTO.notification;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private String type;
    private String content;
    private boolean isRead;
    private LocalDateTime createdAt;
}
