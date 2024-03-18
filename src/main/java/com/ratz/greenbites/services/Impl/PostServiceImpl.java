package com.ratz.greenbites.services.Impl;

import com.ratz.greenbites.DTO.CreatePostDTO;
import com.ratz.greenbites.entity.Post;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.repository.PostRepository;
import com.ratz.greenbites.repository.UserRepository;
import com.ratz.greenbites.services.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public Post createPost(CreatePostDTO post, Long userId) {

        log.info("Creating post for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        Post newPost = new Post();
        newPost.setContent(post.getContent());
        newPost.setImageUrls(post.getImageUrls());
        newPost.setUser(user);
        newPost.setCreatedAt(LocalDateTime.now());

        return postRepository.save(newPost);
    }

    @Override
    @Transactional
    public Post updatePost(Long postId, CreatePostDTO post, Long userId) {

        log.info("Updating post with ID: {}", postId);

        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        if (!existingPost.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to update this post");
        }

        existingPost.setContent(post.getContent());
        existingPost.setImageUrls(post.getImageUrls());

        return postRepository.save(existingPost);
    }

    @Override
    public void deletePost(Long postId, Long userId) {

        log.info("Deleting post with ID: {}", postId);

        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));

        if (!existingPost.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to delete this post");
        }

        postRepository.deleteById(postId);
    }

    @Override
    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
    }
}
