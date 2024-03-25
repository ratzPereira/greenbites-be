package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.post.CommentPostDTO;
import com.ratz.greenbites.DTO.recipe.CreateRecipeDTO;
import com.ratz.greenbites.DTO.recipe.RecipeDTO;
import com.ratz.greenbites.DTO.recipe.UpdateRecipeDTO;
import com.ratz.greenbites.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeService {


    RecipeDTO createRecipe(CreateRecipeDTO createRecipeDTO, Long userId);

    Page<Recipe> findAllRecipes(Pageable pageable);

    RecipeDTO findRecipeById(Long recipeId);

    RecipeDTO updateRecipe(Long recipeId, UpdateRecipeDTO updateRecipeDTO, Long userId);

    void deleteRecipe(Long recipeId, Long userId);

    boolean addRecipeToCollection(Long recipeId, Long collectionId, Long userId);

    boolean removeRecipeFromCollection(Long recipeId, Long collectionId, Long userId);

    boolean toggleLikeRecipe(Long recipeId, Long userId);

    CommentPostDTO addCommentToRecipe(Long recipeId, Long userId, String comment);

    //get comments for recipe
    Page<CommentPostDTO> findCommentsForRecipe(Long recipeId, Pageable pageable);

    Page<Recipe> findPublicRecipesByUserId(Long userId, Pageable pageable);

}
