package com.ratz.greenbites.DTO.privateMessage;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PrivateMessageDTO {

    private Long id;
    private Long senderId;
    private Long recipientId;
    private String subject;
    private String content;
    private boolean isRead;
    private LocalDateTime createdAt;
}
