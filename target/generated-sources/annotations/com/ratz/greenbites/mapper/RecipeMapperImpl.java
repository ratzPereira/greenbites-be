package com.ratz.greenbites.mapper;

import com.ratz.greenbites.DTO.recipe.CreateRecipeDTO;
import com.ratz.greenbites.DTO.recipe.RecipeDTO;
import com.ratz.greenbites.DTO.recipe.UpdateRecipeDTO;
import com.ratz.greenbites.entity.Recipe;
import com.ratz.greenbites.entity.Tag;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-25T22:23:16-0100",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 18.0.2 (Amazon.com Inc.)"
)
@Component
public class RecipeMapperImpl implements RecipeMapper {

    @Override
    public RecipeDTO toRecipeDTO(Recipe recipe) {
        if ( recipe == null ) {
            return null;
        }

        RecipeDTO recipeDTO = new RecipeDTO();

        recipeDTO.setId( recipe.getId() );
        recipeDTO.setTitle( recipe.getTitle() );
        recipeDTO.setDescription( recipe.getDescription() );
        recipeDTO.setPrepTime( recipe.getPrepTime() );
        recipeDTO.setCookTime( recipe.getCookTime() );
        List<String> list = recipe.getInstructions();
        if ( list != null ) {
            recipeDTO.setInstructions( new ArrayList<String>( list ) );
        }
        recipeDTO.setDifficulty( recipe.getDifficulty() );
        Set<Tag> set = recipe.getTags();
        if ( set != null ) {
            recipeDTO.setTags( new HashSet<Tag>( set ) );
        }
        List<String> list1 = recipe.getImages();
        if ( list1 != null ) {
            recipeDTO.setImages( new ArrayList<String>( list1 ) );
        }
        recipeDTO.setPublic( recipe.isPublic() );

        return recipeDTO;
    }

    @Override
    public Recipe toRecipe(CreateRecipeDTO createRecipeDTO) {
        if ( createRecipeDTO == null ) {
            return null;
        }

        Recipe recipe = new Recipe();

        recipe.setTitle( createRecipeDTO.getTitle() );
        recipe.setDescription( createRecipeDTO.getDescription() );
        recipe.setPrepTime( createRecipeDTO.getPrepTime() );
        recipe.setCookTime( createRecipeDTO.getCookTime() );
        List<String> list = createRecipeDTO.getInstructions();
        if ( list != null ) {
            recipe.setInstructions( new ArrayList<String>( list ) );
        }
        recipe.setDifficulty( createRecipeDTO.getDifficulty() );
        Set<Tag> set = createRecipeDTO.getTags();
        if ( set != null ) {
            recipe.setTags( new HashSet<Tag>( set ) );
        }
        recipe.setPublic( createRecipeDTO.isPublic() );

        return recipe;
    }

    @Override
    public void updateRecipeFromDto(UpdateRecipeDTO dto, Recipe entity) {
        if ( dto == null ) {
            return;
        }

        entity.setTitle( dto.getTitle() );
        entity.setDescription( dto.getDescription() );
        entity.setPrepTime( dto.getPrepTime() );
        entity.setCookTime( dto.getCookTime() );
        if ( entity.getInstructions() != null ) {
            List<String> list = dto.getInstructions();
            if ( list != null ) {
                entity.getInstructions().clear();
                entity.getInstructions().addAll( list );
            }
            else {
                entity.setInstructions( null );
            }
        }
        else {
            List<String> list = dto.getInstructions();
            if ( list != null ) {
                entity.setInstructions( new ArrayList<String>( list ) );
            }
        }
        entity.setDifficulty( dto.getDifficulty() );
        if ( entity.getTags() != null ) {
            Set<Tag> set = dto.getTags();
            if ( set != null ) {
                entity.getTags().clear();
                entity.getTags().addAll( set );
            }
            else {
                entity.setTags( null );
            }
        }
        else {
            Set<Tag> set = dto.getTags();
            if ( set != null ) {
                entity.setTags( new HashSet<Tag>( set ) );
            }
        }
        entity.setPublic( dto.isPublic() );
    }
}
