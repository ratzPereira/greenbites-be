package com.ratz.greenbites.DTO.post;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreatePostDTO {

    private List<String> imageUrls;

    @NotEmpty(message = "Content cannot be empty")
    private String content;
}
