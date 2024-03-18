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

}
