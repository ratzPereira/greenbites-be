package com.ratz.greenbites.DTO.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseDTO {

    private Long id;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
}
