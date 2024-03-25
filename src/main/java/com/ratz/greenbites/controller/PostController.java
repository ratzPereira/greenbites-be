package com.ratz.greenbites.controller;

import com.ratz.greenbites.DTO.post.CreatePostDTO;
import com.ratz.greenbites.DTO.post.PostResponseDTO;
import com.ratz.greenbites.entity.Post;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.mapper.PostMapper;
import com.ratz.greenbites.response.HttpResponse;
import com.ratz.greenbites.services.PostService;
import com.ratz.greenbites.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.ratz.greenbites.utils.AppConstants.*;

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

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PostResponseDTO>> getPostByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER, value = "pageNumber", required = false) int pageNumber,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE, value = "pageSize", required = false) int pageSize,
            @RequestParam(defaultValue = DEFAULT_SORT_BY, value = "sortBy", required = false) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_DIRECTION, value = "sortDir", required = false) String sortDir
    ) {

        Page<Post> posts = postService.getPostByUserId(userId, pageNumber, pageSize, sortBy, sortDir);

        Page<PostResponseDTO> pagePostDTOs = posts.map(this::convertToDto);

        return ResponseEntity.ok(pagePostDTOs);
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

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(@PathVariable Long postId) {

        User user = getAuthenticatedUser();
        boolean liked = postService.likeOrDislikePost(postId, user.getId());

        String message = liked ? "Post liked successfully" : "Like removed successfully";
        return ResponseEntity.ok().body(message);
    }

    @PostMapping("/{postId}/addToCollection/{collectionId}")
    public ResponseEntity<HttpResponse> addPostToCollection(@PathVariable Long postId, @PathVariable Long collectionId) {
        User user = getAuthenticatedUser();
        postService.addPostToCollection(user.getId(), postId, collectionId);
        return ResponseEntity.ok().body(HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .message("Post added to collection successfully")
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .build());
    }

    @DeleteMapping("/{postId}/removeFromCollection/{collectionId}")
    public ResponseEntity<HttpResponse> removePostFromCollection(@PathVariable Long postId, @PathVariable Long collectionId) {
        User user = getAuthenticatedUser();
        postService.removePostFromCollection(user.getId(), postId, collectionId);
        return ResponseEntity.ok().body(HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .message("Post removed from collection successfully")
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .build());
    }

    //helper methods
    private PostResponseDTO convertToDto(Post post) {
        User user = userService.getUserById(post.getUser().getId());
        return buildPostResponse(user, post);
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
