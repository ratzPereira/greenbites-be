package com.ratz.greenbites.controller;

import com.ratz.greenbites.DTO.comment.CommentResponseDTO;
import com.ratz.greenbites.DTO.comment.CreateCommentDTO;
import com.ratz.greenbites.entity.Comment;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.services.CommentService;
import com.ratz.greenbites.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
@Slf4j
@Tag(name = "Comments", description = "Operations related to comments on posts and recipes")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @PostMapping("/{postId}/comments")
    @Operation(summary = "Create a Comment", description = "Create a comment on a specified post.")
    @ApiResponse(responseCode = "201", description = "Comment created", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponseDTO.class))})
    public ResponseEntity<CommentResponseDTO> createComment(@PathVariable Long postId, @Valid @RequestBody CreateCommentDTO createCommentDTO) {

        log.info("Received request to create comment for post with id: {}", postId);
        User user = getAuthenticatedUser();
        Comment comment = commentService.createComment(createCommentDTO, postId, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(buildCommentResponse(comment));
    }

    @PutMapping("/{postId}/comments/{commentId}")
    @Operation(summary = "Update a Comment", description = "Update a specific comment by comment ID.")
    @ApiResponse(responseCode = "200", description = "Comment updated", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CommentResponseDTO.class))})
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable Long commentId, @Valid @RequestBody CreateCommentDTO createCommentDTO) {

        log.info("Received request to update comment with id: {}", commentId);
        User user = getAuthenticatedUser();
        Comment comment = commentService.updateComment(createCommentDTO.getContent(), commentId, user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(buildCommentResponse(comment));
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    @Operation(summary = "Delete a Comment", description = "Delete a specific comment by comment ID.")
    @ApiResponse(responseCode = "204", description = "Comment deleted")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {

        log.info("Received request to delete comment with id: {}", commentId);
        User user = getAuthenticatedUser();
        commentService.deleteComment(commentId, user.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{postId}/comments/{commentId}/like")
    @Operation(summary = "Like/Unlike a Comment", description = "Toggle like on a comment. Likes if not liked before, unlikes if already liked.")
    @ApiResponse(responseCode = "200", description = "Like toggled")
    public ResponseEntity<?> toggleLike(@PathVariable Long commentId) {

        log.info("Received request to like comment with id: {}", commentId);
        User user = getAuthenticatedUser();
        boolean liked = commentService.toggleLike(commentId, user.getId());

        return ResponseEntity.ok(liked ? "Comment liked" : "Like removed from comment");
    }

    @GetMapping("/{postId}/comments/")
    @Operation(summary = "Get Comments by Post ID", description = "Retrieve all comments for a given post, with pagination.")
    @ApiResponse(responseCode = "200", description = "Comments fetched", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CommentResponseDTO.class)))})
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsByPostId(@PathVariable Long postId,
                                                                        @RequestParam(value = "page", defaultValue = "0") int page,
                                                                        @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<Comment> commentPage = commentService.getCommentsByPostId(postId, page, size);
        Page<CommentResponseDTO> dtoPage = commentPage.map(this::buildCommentResponse);
        return ResponseEntity.ok(dtoPage);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userService.getUserByEmail(currentUsername);
    }

    private CommentResponseDTO buildCommentResponse(Comment comment) {

        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setUserId(comment.getUser().getId());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setLikeCount(comment.getLikedBy().size());

        return dto;
    }
}
