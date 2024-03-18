package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.CreatePostDTO;
import com.ratz.greenbites.entity.Post;

public interface PostService {

    Post createPost(CreatePostDTO post, Long userId);

    Post updatePost(Long postId, CreatePostDTO post, Long userId);

    void deletePost(Long postId, Long userId);

    Post getPostById(Long postId);
}
