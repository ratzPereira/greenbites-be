package com.ratz.greenbites.controller;

import com.ratz.greenbites.DTO.follower.UserSummaryDTO;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.response.HttpResponse;
import com.ratz.greenbites.services.UserFollowerService;
import com.ratz.greenbites.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/followers")
@Slf4j
public class UserFollowerController {


    private final UserFollowerService followerService;
    private final UserService userService;


    @GetMapping("/{userId}/followers")
    public ResponseEntity<HttpResponse> getFollowers(@PathVariable Long userId,
                                                     Pageable pageable) {
        Page<UserSummaryDTO> followers = followerService.getFollowers(userId, pageable);
        return buildPageResponse(followers, "List of followers retrieved successfully.");
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<HttpResponse> getFollowing(@PathVariable Long userId,
                                                     Pageable pageable) {
        Page<UserSummaryDTO> following = followerService.getFollowing(userId, pageable);
        return buildPageResponse(following, "List of following retrieved successfully.");
    }

    @PostMapping("/follow/{userId}")
    public ResponseEntity<HttpResponse> followUser(@PathVariable Long userId) {

        log.info("Received request to follow user with id: {}", userId);
        User user = getAuthenticatedUser();
        followerService.followUser(user.getId(), userId);
        return buildResponse(HttpStatus.OK, "You started following the user.");
    }

    @DeleteMapping("/unfollow/{userId}")
    public ResponseEntity<HttpResponse> unfollowUser(@PathVariable Long userId) {

        log.info("Received request to unfollow user with id: {}", userId);
        User user = getAuthenticatedUser();
        followerService.unfollowUser(user.getId(), userId);
        return buildResponse(HttpStatus.OK, "You have unfollowed the user.");
    }


    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userService.getUserByEmail(currentUsername);
    }

    private ResponseEntity<HttpResponse> buildResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .statusCode(status.value())
                        .httpStatus(status)
                        .message(message)
                        .build());
    }

    private ResponseEntity<HttpResponse> buildPageResponse(Page<UserSummaryDTO> page, String message) {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("users", page.getContent());
        responseData.put("totalItems", page.getTotalElements());
        responseData.put("totalPages", page.getTotalPages());

        return ResponseEntity.ok()
                .body(HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(responseData)
                        .message(message)
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .build());
    }
}
