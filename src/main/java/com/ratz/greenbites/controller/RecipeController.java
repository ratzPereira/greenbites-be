package com.ratz.greenbites.controller;

import com.ratz.greenbites.DTO.comment.CreateCommentDTO;
import com.ratz.greenbites.DTO.post.CommentPostDTO;
import com.ratz.greenbites.DTO.recipe.CreateRecipeDTO;
import com.ratz.greenbites.DTO.recipe.RecipeDTO;
import com.ratz.greenbites.DTO.recipe.UpdateRecipeDTO;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.mapper.RecipeMapper;
import com.ratz.greenbites.response.HttpResponse;
import com.ratz.greenbites.services.RecipeService;
import com.ratz.greenbites.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/recipes")
@Tag(name = "Recipes", description = "The Recipes API for managing user recipes.")
public class RecipeController {

    private final RecipeService recipeService;
    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create a new recipe", description = "Allows users to create a new recipe.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recipe created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input")})
    public ResponseEntity<HttpResponse> createRecipe(@RequestBody CreateRecipeDTO createRecipeDTO) {
        User user = getAuthenticatedUser();
        RecipeDTO recipeDTO = recipeService.createRecipe(createRecipeDTO, user.getId());
        return buildResponse("Recipe created successfully", Map.of("recipe", recipeDTO), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "List all recipes", description = "Retrieves a paginated list of all recipes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipes fetched successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))})})
    public ResponseEntity<HttpResponse> listAllRecipes(Pageable pageable) {
        Page<RecipeDTO> recipes = recipeService.findAllRecipes(pageable).map(RecipeMapper.INSTANCE::toRecipeDTO);
        return buildPageResponse(recipes, "All recipes fetched successfully", "recipes");
    }

    @GetMapping("/{recipeId}")
    @Operation(summary = "Get recipe by ID", description = "Retrieves a single recipe by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe fetched successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Recipe not found")})
    public ResponseEntity<HttpResponse> getRecipeById(@PathVariable Long recipeId) {
        RecipeDTO recipeDTO = recipeService.findRecipeById(recipeId);
        return buildResponse("Recipe fetched successfully", Map.of("recipe", recipeDTO), HttpStatus.OK);
    }

    @PutMapping("/{recipeId}")
    @Operation(summary = "Update a recipe", description = "Allows the creator of the recipe to update it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Recipe not found")})
    public ResponseEntity<HttpResponse> updateRecipe(@PathVariable Long recipeId, @RequestBody UpdateRecipeDTO updateRecipeDTO) {
        User user = getAuthenticatedUser();
        RecipeDTO recipeDTO = recipeService.updateRecipe(recipeId, updateRecipeDTO, user.getId());
        return buildResponse("Recipe updated successfully", Map.of("recipe", recipeDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{recipeId}")
    @Operation(summary = "Delete a recipe", description = "Allows the creator of the recipe to delete it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Recipe not found")})
    public ResponseEntity<HttpResponse> deleteRecipe(@PathVariable Long recipeId) {
        User user = getAuthenticatedUser();
        recipeService.deleteRecipe(recipeId, user.getId());
        return buildResponse("Recipe deleted successfully", null, HttpStatus.OK);
    }

    @PostMapping("/{recipeId}/toggle-like")
    @Operation(summary = "Like or unlike a recipe", description = "Allows a user to like or unlike a recipe.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully liked or unliked the recipe")})
    public ResponseEntity<HttpResponse> toggleLikeRecipe(@PathVariable Long recipeId) {
        User user = getAuthenticatedUser();
        boolean isLiked = recipeService.toggleLikeRecipe(recipeId, user.getId());

        String message = isLiked ? "Recipe liked successfully" : "Like removed from recipe successfully";
        return buildResponse(message, null, HttpStatus.OK);
    }

    @PostMapping("/{recipeId}/comments")
    @Operation(summary = "Add a comment to a recipe", description = "Allows users to add comments to a recipe.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment added successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentPostDTO.class))})})
    public ResponseEntity<HttpResponse> addCommentToRecipe(@PathVariable Long recipeId, @RequestBody CreateCommentDTO createCommentDTO) {
        User user = getAuthenticatedUser();
        CommentPostDTO commentDTO = recipeService.addCommentToRecipe(recipeId, user.getId(), createCommentDTO.getContent());
        return buildResponse("Comment added to recipe successfully", Map.of("comment", commentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{recipeId}/comments")
    @Operation(summary = "List comments for a recipe", description = "Retrieves a paginated list of comments for a specific recipe.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments fetched successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))})})
    public ResponseEntity<HttpResponse> listCommentsForRecipe(@PathVariable Long recipeId, Pageable pageable) {
        Page<CommentPostDTO> comments = recipeService.findCommentsForRecipe(recipeId, pageable);
        return buildPageResponse(comments, "Comments for recipe fetched successfully", "comments");
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "List public recipes by user ID", description = "Retrieves a paginated list of public recipes created by a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User public recipes fetched successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))})})
    public ResponseEntity<HttpResponse> listUserPublicRecipes(@PathVariable Long userId, Pageable pageable) {
        Page<RecipeDTO> recipes = recipeService.findPublicRecipesByUserId(userId, pageable).map(RecipeMapper.INSTANCE::toRecipeDTO);
        return buildPageResponse(recipes, "User public recipes fetched successfully", "recipes");
    }

    @PostMapping("/{recipeId}/add-to-collection/{collectionId}")
    @Operation(summary = "Add a recipe to a collection", description = "Allows users to add a recipe to one of their collections.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe added to collection successfully",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Recipe or collection not found")})
    public ResponseEntity<HttpResponse> addRecipeToCollection(@PathVariable Long recipeId, @PathVariable Long collectionId) {
        User user = getAuthenticatedUser();
        boolean added = recipeService.addRecipeToCollection(user.getId(), recipeId, collectionId);

        String message = added ? "Recipe added to collection successfully" : "Recipe is already in the collection";
        return buildResponse(message, null, HttpStatus.OK);
    }

    @DeleteMapping("/{recipeId}/remove-from-collection/{collectionId}")
    @Operation(summary = "Remove a recipe from a collection", description = "Allows users to remove a recipe from one of their collections.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe removed from collection successfully",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Recipe or collection not found")})
    public ResponseEntity<HttpResponse> removeRecipeFromCollection(@PathVariable Long recipeId, @PathVariable Long collectionId) {
        User user = getAuthenticatedUser();
        boolean removed = recipeService.removeRecipeFromCollection(user.getId(), recipeId, collectionId);

        String message = removed ? "Recipe removed from collection successfully" : "Recipe was not in the collection";
        return buildResponse(message, null, HttpStatus.OK);
    }

    private ResponseEntity<HttpResponse> buildResponse(String message, Map<String, Object> data, HttpStatus status) {
        Map<String, Object> responseData = new HashMap<>();
        if (data != null) {
            responseData.putAll(data);
        }

        HttpResponse response = HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .message(message)
                .statusCode(status.value())
                .httpStatus(status)
                .data(responseData)
                .build();

        return new ResponseEntity<>(response, status);
    }

    private ResponseEntity<HttpResponse> buildPageResponse(Page<?> page, String message, String dataType) {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put(dataType, page.getContent());
        responseData.put("totalItems", page.getTotalElements());
        responseData.put("totalPages", page.getTotalPages());

        return buildResponse(message, responseData, HttpStatus.OK);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userService.getUserByEmail(currentUsername);
    }
}
