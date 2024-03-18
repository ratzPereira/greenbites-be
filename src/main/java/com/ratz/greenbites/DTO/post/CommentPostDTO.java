package com.ratz.greenbites.DTO.post;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentPostDTO {

    private Long id;
    private Long postId;
    private String firstName;
    private String lastName;
    private String content;
    private LocalDateTime createdAt;
}
