package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.post.CreatePostDTO;
import com.ratz.greenbites.entity.Post;
import org.springframework.data.domain.Page;

public interface PostService {

    Post createPost(CreatePostDTO post, Long userId);

    Post updatePost(Long postId, CreatePostDTO post, Long userId);

    void deletePost(Long postId, Long userId);

    Post getPostById(Long postId);

    Page<Post> getPostByUserId(Long userId, int pageNumber, int pageSize, String sortBy, String sortDir);

    boolean likeOrDislikePost(Long postId, Long userId);

    //removePostFromCollection
    void removePostFromCollection(Long postId, Long collectionId, Long userId);

    //addPostToCollection
    void addPostToCollection(Long postId, Long collectionId, Long userId);
}
