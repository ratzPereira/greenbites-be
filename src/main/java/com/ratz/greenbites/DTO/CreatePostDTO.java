package com.ratz.greenbites.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreatePostDTO {

    @NotEmpty(message = "Content cannot be empty")
    private String content;
}
