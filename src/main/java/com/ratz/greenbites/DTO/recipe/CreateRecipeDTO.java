package com.ratz.greenbites.DTO.recipe;

import com.ratz.greenbites.enums.Difficulty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Data
public class CreateRecipeDTO {
    private String title;
    private String description;
    private int prepTime;
    private int cookTime;
    private List<String> instructions;
    private Difficulty difficulty;
    private Set<String> tags;
    private List<MultipartFile> imageFiles;
    private boolean isPublic;
}
