package com.ratz.greenbites.controller;

import com.ratz.greenbites.DTO.CreatePostDTO;
import com.ratz.greenbites.DTO.PostResponseDTO;
import com.ratz.greenbites.entity.Post;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.mapper.PostMapper;
import com.ratz.greenbites.services.PostService;
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
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@Valid @RequestBody CreatePostDTO createPostDTO) {

        User user = getAuthenticatedUser();
        Post post = postService.createPost(createPostDTO, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(buildPostResponse(user, post));


    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long postId) {

        User user = getAuthenticatedUser();
        Post post = postService.getPostById(postId);
        return ResponseEntity.ok(buildPostResponse(user, post));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long postId, @Valid @RequestBody CreatePostDTO createPostDTO) {

        User user = getAuthenticatedUser();
        Post post = postService.updatePost(postId, createPostDTO, user.getId());
        return ResponseEntity.ok(buildPostResponse(user, post));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {

        User user = getAuthenticatedUser();
        postService.deletePost(postId, user.getId());
        return ResponseEntity.status(HttpStatus.OK).body("Post deleted successfully");
    }


    private User getAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userService.getUserByEmail(currentUsername);
    }


    private PostResponseDTO buildPostResponse(User user, Post post) {

        PostResponseDTO postResponseDTO = PostMapper.INSTANCE.postToPostDTO(post);
        postResponseDTO.setFirstName(user.getProfile().getFirstName());
        postResponseDTO.setLastName(user.getProfile().getLastName());
        postResponseDTO.setCommentCount(post.getComments().size());
        postResponseDTO.setImageUrls(post.getImageUrls());
        postResponseDTO.setLikeCount(post.getLikedBy().size());

        return postResponseDTO;
    }
}
