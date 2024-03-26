package com.ratz.greenbites.controller;


import com.ratz.greenbites.DTO.notification.NotificationDTO;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.mapper.NotificationMapper;
import com.ratz.greenbites.response.HttpResponse;
import com.ratz.greenbites.services.NotificationService;
import com.ratz.greenbites.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notifications")
@Slf4j
@Tag(name = "Notifications", description = "The Notifications API for managing user notifications.")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;


    @GetMapping
    @Operation(summary = "Get all notifications", description = "Retrieves all notifications for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications fetched successfully", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    public ResponseEntity<HttpResponse> getAllNotifications(Pageable pageable) {
        User userPrincipal = getAuthenticatedUser();
        Page<NotificationDTO> notifications = notificationService.getAllUserNotifications(userPrincipal.getId(), pageable)
                .map(NotificationMapper.INSTANCE::toDto);

        return buildPageResponse(notifications, "All notifications fetched successfully.");
    }

    @GetMapping("/unread")
    @Operation(summary = "Get unread notifications", description = "Retrieves all unread notifications for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unread notifications fetched successfully", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    public ResponseEntity<HttpResponse> getUnreadNotifications(Pageable pageable) {
        User userPrincipal = getAuthenticatedUser();
        Page<NotificationDTO> notifications = notificationService.getUnreadUserNotifications(userPrincipal.getId(), pageable)
                .map(NotificationMapper.INSTANCE::toDto);

        return buildPageResponse(notifications, "Unread notifications fetched successfully.");
    }

    @PatchMapping("/read/all")
    @Operation(summary = "Mark all notifications as read", description = "Marks all notifications as read for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All notifications marked as read"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")})
    public ResponseEntity<HttpResponse> markAllNotificationsAsRead() {
        User userPrincipal = getAuthenticatedUser();
        notificationService.markAllNotificationsAsRead(userPrincipal.getId());

        return buildSimpleResponse("All notifications marked as read");
    }

    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "Mark a notification as read", description = "Marks a specific notification as read based on its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification marked as read"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Notification not found")})
    public ResponseEntity<HttpResponse> markAsRead(@PathVariable Long notificationId) {
        notificationService.markNotificationAsRead(notificationId);

        return buildSimpleResponse("Notification marked as read");
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userService.getUserByEmail(currentUsername);
    }

    private ResponseEntity<HttpResponse> buildSimpleResponse(String message) {
        return ResponseEntity.ok(HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .message(message)
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .build());
    }

    private ResponseEntity<HttpResponse> buildPageResponse(Page<NotificationDTO> page, String message) {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("notifications", page.getContent());
        responseData.put("totalItems", page.getTotalElements());
        responseData.put("totalPages", page.getTotalPages());

        return ResponseEntity.ok(HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .data(responseData)
                .message(message)
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .build());
    }
}
