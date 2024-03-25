package com.ratz.greenbites.mapper;

import com.ratz.greenbites.DTO.recipe.CreateRecipeDTO;
import com.ratz.greenbites.DTO.recipe.RecipeDTO;
import com.ratz.greenbites.DTO.recipe.UpdateRecipeDTO;
import com.ratz.greenbites.entity.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RecipeMapper {
    RecipeMapper INSTANCE = Mappers.getMapper(RecipeMapper.class);

    RecipeDTO toRecipeDTO(Recipe recipe);

    Recipe toRecipe(CreateRecipeDTO createRecipeDTO);

    void updateRecipeFromDto(UpdateRecipeDTO dto, @MappingTarget Recipe entity);
}