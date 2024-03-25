package com.ratz.greenbites.DTO.collection;

import lombok.Data;

import java.util.Set;

@Data
public class CollectionDTO {
    private Long id;
    private String name;
    private Set<Long> recipeIds;
    private Set<Long> postIds;
}