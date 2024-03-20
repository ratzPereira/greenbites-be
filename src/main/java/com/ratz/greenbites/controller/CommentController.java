package com.ratz.greenbites.controller;

import com.ratz.greenbites.DTO.comment.CommentResponseDTO;
import com.ratz.greenbites.DTO.comment.CreateCommentDTO;
import com.ratz.greenbites.entity.Comment;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.services.CommentService;
import com.ratz.greenbites.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponseDTO> createComment(@PathVariable Long postId, @Valid @RequestBody CreateCommentDTO createCommentDTO) {

        log.info("Received request to create comment for post with id: {}", postId);
        User user = getAuthenticatedUser();
        Comment comment = commentService.createComment(createCommentDTO, postId, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(buildCommentResponse(comment));
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable Long commentId, @Valid @RequestBody CreateCommentDTO createCommentDTO) {

        log.info("Received request to update comment with id: {}", commentId);
        User user = getAuthenticatedUser();
        Comment comment = commentService.updateComment(createCommentDTO.getContent(), commentId, user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(buildCommentResponse(comment));
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {

        log.info("Received request to delete comment with id: {}", commentId);
        User user = getAuthenticatedUser();
        commentService.deleteComment(commentId, user.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{postId}/comments/{commentId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long commentId) {

        log.info("Received request to like comment with id: {}", commentId);
        User user = getAuthenticatedUser();
        boolean liked = commentService.toggleLike(commentId, user.getId());

        return ResponseEntity.ok(liked ? "Comment liked" : "Like removed from comment");
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

        return dto;
    }
}
