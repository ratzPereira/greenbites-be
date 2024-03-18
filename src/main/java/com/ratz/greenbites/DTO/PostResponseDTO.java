package com.ratz.greenbites.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponseDTO {

    private Long id;
    private String content;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private int likeCount;
    private int commentCount;
}
