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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/posts")
@Slf4j
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@Valid @RequestBody CreatePostDTO createPostDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userService.getUserByEmail(currentUsername);

        Post post = postService.createPost(createPostDTO, user.getId());

        return buildPostResponse(user, post);

    }


    private ResponseEntity<PostResponseDTO> buildPostResponse(User user, Post post) {

        PostResponseDTO postResponseDTO = PostMapper.INSTANCE.postToPostDTO(post);

        postResponseDTO.setFirstName(user.getProfile().getFirstName());
        postResponseDTO.setLastName(user.getProfile().getLastName());
        postResponseDTO.setCommentCount(post.getComments().size());
        postResponseDTO.setLikeCount(post.getLikedBy().size());

        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDTO);
    }
}
