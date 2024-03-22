package com.ratz.greenbites.DTO.post;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CreatePostDTO {

    private List<MultipartFile> imageFiles;

    @NotEmpty(message = "Content cannot be empty")
    private String content;
}
