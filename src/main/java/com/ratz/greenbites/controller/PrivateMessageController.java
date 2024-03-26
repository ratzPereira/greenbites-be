package com.ratz.greenbites.controller;

import com.ratz.greenbites.DTO.privateMessage.PrivateMessageCreateDTO;
import com.ratz.greenbites.DTO.privateMessage.PrivateMessageDTO;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.response.HttpResponse;
import com.ratz.greenbites.services.PrivateMessageService;
import com.ratz.greenbites.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static java.time.LocalTime.now;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/private-messages")
@Slf4j
@Tag(name = "Private Messages", description = "The Private Messages API for sending and receiving private messages.")
public class PrivateMessageController {

    private final PrivateMessageService privateMessageService;
    private final UserService userService;


    @PostMapping
    @Operation(summary = "Send Private Message", description = "Sends a private message to another user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Private message sent successfully", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid message data")})
    public ResponseEntity<PrivateMessageDTO> sendPrivateMessage(@Valid @RequestBody PrivateMessageCreateDTO privateMessageCreateDTO) {

        log.info("Received request to send private message");
        User user = getAuthenticatedUser();

        PrivateMessageDTO privateMessageDTO = privateMessageService.sendPrivateMessage(privateMessageCreateDTO, user);
        return ResponseEntity.ok(privateMessageDTO);
    }

    @GetMapping("/sent")
    @Operation(summary = "Get Sent Messages", description = "Retrieves the messages sent by the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sent messages fetched successfully", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Messages not found")})
    public ResponseEntity<HttpResponse> getSentMessages(Pageable pageable) {

        log.info("Received request to get sent messages");
        User user = getAuthenticatedUser();
        Page<PrivateMessageDTO> sentMessages = privateMessageService.getSentMessages(user.getId(), pageable);
        return buildPageResponse(sentMessages, "Sent messages fetched successfully");
    }

    @GetMapping("/received")
    @Operation(summary = "Get Received Messages", description = "Retrieves the messages received by the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Received messages fetched successfully", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Messages not found")})
    public ResponseEntity<HttpResponse> getReceivedMessages(Pageable pageable) {

        log.info("Received request to get received messages");
        User user = getAuthenticatedUser();
        Page<PrivateMessageDTO> receivedMessages = privateMessageService.getReceivedMessages(user.getId(), pageable);
        return buildPageResponse(receivedMessages, "Received messages fetched successfully");
    }

    @GetMapping("/{messageId}")
    @Operation(summary = "Read Message", description = "Marks a message as read and retrieves its contents.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message read successfully", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Message not found")})
    public ResponseEntity<HttpResponse> readMessage(@PathVariable Long messageId) {

        log.info("Received request to read message");
        User user = getAuthenticatedUser();
        PrivateMessageDTO messageDTO = privateMessageService.readMessage(messageId, user.getId());
        return ResponseEntity.ok().body(HttpResponse.builder()
                .timeStamp(now().toString())
                .data(Map.of("message", messageDTO))
                .message("Message read successfully")
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .build());
    }

    @DeleteMapping("/{messageId}")
    @Operation(summary = "Delete Message", description = "Deletes a specific message.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Message deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Message not found")})
    public ResponseEntity<HttpResponse> deleteMessage(@PathVariable Long messageId) {

        log.info("Received request to delete message");
        User user = getAuthenticatedUser();
        privateMessageService.deleteMessage(messageId, user.getId());
        return ResponseEntity.ok().body(HttpResponse.builder()
                .timeStamp(now().toString())
                .message("Message deleted successfully")
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .build());
    }

    private ResponseEntity<HttpResponse> buildPageResponse(Page<PrivateMessageDTO> messages, String message) {
        return ResponseEntity.ok().body(HttpResponse.builder()
                .timeStamp(now().toString())
                .data(Map.of("messages", messages))
                .message(message)
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .build());
    }


    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userService.getUserByEmail(currentUsername);
    }

}


