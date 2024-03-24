package com.ratz.greenbites.controller;


import com.ratz.greenbites.DTO.notification.NotificationDTO;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.mapper.NotificationMapper;
import com.ratz.greenbites.response.HttpResponse;
import com.ratz.greenbites.services.NotificationService;
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
@RequestMapping("/api/v1/notifications")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;


    @GetMapping
    public ResponseEntity<HttpResponse> getAllNotifications(Pageable pageable) {
        User userPrincipal = getAuthenticatedUser();
        Page<NotificationDTO> notifications = notificationService.getAllUserNotifications(userPrincipal.getId(), pageable)
                .map(NotificationMapper.INSTANCE::toDto);

        return buildPageResponse(notifications, "All notifications fetched successfully.");
    }

    @GetMapping("/unread")
    public ResponseEntity<HttpResponse> getUnreadNotifications(Pageable pageable) {
        User userPrincipal = getAuthenticatedUser();
        Page<NotificationDTO> notifications = notificationService.getUnreadUserNotifications(userPrincipal.getId(), pageable)
                .map(NotificationMapper.INSTANCE::toDto);

        return buildPageResponse(notifications, "Unread notifications fetched successfully.");
    }

    @PatchMapping("/read/all")
    public ResponseEntity<HttpResponse> markAllNotificationsAsRead() {
        User userPrincipal = getAuthenticatedUser();
        notificationService.markAllNotificationsAsRead(userPrincipal.getId());

        return buildSimpleResponse("All notifications marked as read");
    }

    @PatchMapping("/{notificationId}/read")
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
