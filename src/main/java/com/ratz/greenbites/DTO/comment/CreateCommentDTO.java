package com.ratz.greenbites.DTO.comment;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateCommentDTO {

    @NotEmpty(message = "Content cannot be empty")
    private String content;
}
