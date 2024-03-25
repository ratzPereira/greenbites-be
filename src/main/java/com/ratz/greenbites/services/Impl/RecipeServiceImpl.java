package com.ratz.greenbites.services.Impl;

import com.ratz.greenbites.DTO.post.CommentPostDTO;
import com.ratz.greenbites.DTO.recipe.CreateRecipeDTO;
import com.ratz.greenbites.DTO.recipe.RecipeDTO;
import com.ratz.greenbites.DTO.recipe.UpdateRecipeDTO;
import com.ratz.greenbites.entity.Collection;
import com.ratz.greenbites.entity.Comment;
import com.ratz.greenbites.entity.Recipe;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.mapper.RecipeMapper;
import com.ratz.greenbites.repository.CollectionRepository;
import com.ratz.greenbites.repository.CommentRepository;
import com.ratz.greenbites.repository.RecipeRepository;
import com.ratz.greenbites.repository.UserRepository;
import com.ratz.greenbites.services.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final CollectionRepository collectionRepository;
    private final CommentRepository commentRepository;
    private final RecipeMapper recipeMapper;

    @Override
    public RecipeDTO createRecipe(CreateRecipeDTO createRecipeDTO, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Recipe recipe = recipeMapper.toRecipe(createRecipeDTO);
        recipe.setUser(user);

        recipe = recipeRepository.save(recipe);
        return recipeMapper.toRecipeDTO(recipe);
    }

    @Override
    public Page<Recipe> findAllRecipes(Pageable pageable) {
        return recipeRepository.findAll(pageable);
    }

    @Override
    public RecipeDTO findRecipeById(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found"));
        return recipeMapper.toRecipeDTO(recipe);
    }

    @Override
    public RecipeDTO updateRecipe(Long recipeId, UpdateRecipeDTO updateRecipeDTO, Long userId) {
        Recipe recipe = recipeRepository.findByIdAndUserId(recipeId, userId).orElseThrow(() -> new RuntimeException("Recipe not found or not authorized"));
        recipeMapper.updateRecipeFromDto(updateRecipeDTO, recipe);
        recipe = recipeRepository.save(recipe);
        return recipeMapper.toRecipeDTO(recipe);
    }

    @Override
    public void deleteRecipe(Long recipeId, Long userId) {
        Recipe recipe = recipeRepository.findByIdAndUserId(recipeId, userId).orElseThrow(() -> new RuntimeException("Recipe not found or not authorized"));
        recipeRepository.delete(recipe);
    }

    @Override
    public boolean addRecipeToCollection(Long recipeId, Long collectionId, Long userId) {
        Collection collection = collectionRepository.findByIdAndUserId(collectionId, userId).orElseThrow(() -> new RuntimeException("Collection not found or not authorized"));
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found"));
        if (collection.getRecipes().contains(recipe)) {
            return false;
        }
        collection.getRecipes().add(recipe);
        collectionRepository.save(collection);
        return true;
    }

    @Override
    public boolean removeRecipeFromCollection(Long recipeId, Long collectionId, Long userId) {
        Collection collection = collectionRepository.findByIdAndUserId(collectionId, userId).orElseThrow(() -> new RuntimeException("Collection not found or not authorized"));
        Recipe recipe = collection.getRecipes().stream().filter(r -> r.getId().equals(recipeId)).findFirst().orElseThrow(() -> new RuntimeException("Recipe not found in collection"));
        collection.getRecipes().remove(recipe);
        collectionRepository.save(collection);
        return true;
    }

    @Override
    public boolean toggleLikeRecipe(Long recipeId, Long userId) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        boolean isLiked = recipe.getLikedBy().contains(user);
        if (isLiked) {
            recipe.getLikedBy().remove(user);
        } else {
            recipe.getLikedBy().add(user);
        }
        recipeRepository.save(recipe);
        return !isLiked;
    }

    @Override
    public CommentPostDTO addCommentToRecipe(Long recipeId, Long userId, String commentContent) {
        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new RuntimeException("Recipe not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Comment comment = new Comment();

        comment.setContent(commentContent);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        recipe.getComments().add(comment);
        recipeRepository.save(recipe);

        return new CommentPostDTO(
                comment.getId(),
                recipeId,
                user.getProfile().getFirstName(),
                user.getProfile().getLastName(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }

    @Override
    public Page<CommentPostDTO> findCommentsForRecipe(Long recipeId, Pageable pageable) {
        Page<Comment> comments = commentRepository.findByRecipeId(recipeId, pageable);
        return comments.map(comment -> new CommentPostDTO(
                comment.getId(),
                recipeId,
                comment.getUser().getProfile().getFirstName(),
                comment.getUser().getProfile().getLastName(),
                comment.getContent(),
                comment.getCreatedAt()
        ));
    }

    @Override
    public Page<Recipe> findPublicRecipesByUserId(Long userId, Pageable pageable) {
        return recipeRepository.findByUserIdAndIsPublicTrue(userId, pageable);
    }
}
