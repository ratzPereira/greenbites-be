package com.ratz.greenbites.controller;

import com.ratz.greenbites.DTO.post.PostResponseDTO;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.response.HttpResponse;
import com.ratz.greenbites.services.FeedService;
import com.ratz.greenbites.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/feed")
@Slf4j
@Tag(name = "Feed", description = "The Feed API")
public class FeedController {

    private final FeedService feedService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get user feed", description = "Fetches the personalized feed for the authenticated user, including posts from followed users and recommended content.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feed fetched successfully", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<HttpResponse> getFeed(Pageable pageable) {

        log.info("GET request to fetch feed for user.");
        User user = getAuthenticatedUser();
        Page<PostResponseDTO> feed = feedService.getFeedForUser(user.getId(), pageable);

        return buildPageResponse(feed, "Feed fetched successfully.");
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userService.getUserByEmail(currentUsername);
    }

    private ResponseEntity<HttpResponse> buildPageResponse(Page<PostResponseDTO> page, String message) {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("posts", page.getContent());
        responseData.put("totalItems", page.getTotalElements());
        responseData.put("totalPages", page.getTotalPages());

        return ResponseEntity.ok(HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .data(responseData)
                .message(message)
                .statusCode(200)
                .httpStatus(HttpStatus.OK)
                .build());
    }
}
