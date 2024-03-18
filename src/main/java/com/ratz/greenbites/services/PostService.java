package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.CreatePostDTO;
import com.ratz.greenbites.entity.Post;

public interface PostService {

    Post createPost(CreatePostDTO post, Long userId);
}
