package com.ratz.greenbites.DTO.recipe;

import com.ratz.greenbites.entity.Tag;
import com.ratz.greenbites.enums.Difficulty;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class RecipeDTO {
    private Long id;
    private String title;
    private String description;
    private int prepTime;
    private int cookTime;
    private List<String> instructions;
    private Difficulty difficulty;
    private Set<Tag> tags;
    private List<String> images;
    private boolean isPublic;
    private Long userId;
    private int likeCount;
    private int commentCount;
}
