package com.ratz.greenbites.services.Impl;

import com.ratz.greenbites.DTO.comment.CreateCommentDTO;
import com.ratz.greenbites.entity.Comment;
import com.ratz.greenbites.entity.Post;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.repository.CommentRepository;
import com.ratz.greenbites.repository.PostRepository;
import com.ratz.greenbites.repository.UserRepository;
import com.ratz.greenbites.services.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public Comment createComment(CreateCommentDTO comment, Long postId, Long userId) {
        log.info("Creating comment for post with ID: {}", postId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        Comment newComment = new Comment();
        newComment.setContent(comment.getContent());
        newComment.setPost(post);
        newComment.setUser(user);
        newComment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(newComment);
    }

    @Override
    public Comment updateComment(String content, Long commentId, Long userId) {
        log.info("Updating comment with ID: {}", commentId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));

        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("User not authorized to update this comment");
        }

        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        log.info("Deleting comment with ID: {}", commentId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));

        if (!comment.getUser().getId().equals(userId)) {
            throw new RuntimeException("User not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    @Override
    public boolean toggleLike(Long commentId, Long userId) {

        log.info("Toggling like on comment with ID: {}", commentId);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isLiked = comment.getLikedBy().contains(user);

        if (isLiked) {
            comment.getLikedBy().remove(user);
        } else {
            comment.getLikedBy().add(user);
        }

        commentRepository.save(comment);
        return !isLiked;
    }

    @Override
    public Page<Comment> getCommentsByPostId(Long postId, int page, int size) {
        log.info("Fetching comments for post with ID: {}", postId);
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return commentRepository.findByPostId(postId, pageable);
    }
}
