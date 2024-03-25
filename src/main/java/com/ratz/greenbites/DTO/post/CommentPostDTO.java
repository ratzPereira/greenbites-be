package com.ratz.greenbites.DTO.post;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentPostDTO {

    private Long id;
    private Long postId;
    private String firstName;
    private String lastName;
    private String content;
    private LocalDateTime createdAt;

    public CommentPostDTO(Long id, Long postId, String firstName, String lastName, String content, LocalDateTime createdAt) {
        this.id = id;
        this.postId = postId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.content = content;
        this.createdAt = createdAt;
    }

}
