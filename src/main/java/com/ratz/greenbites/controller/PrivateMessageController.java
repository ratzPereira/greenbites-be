package com.ratz.greenbites.controller;

import com.ratz.greenbites.DTO.privateMessage.PrivateMessageCreateDTO;
import com.ratz.greenbites.DTO.privateMessage.PrivateMessageDTO;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.response.HttpResponse;
import com.ratz.greenbites.services.PrivateMessageService;
import com.ratz.greenbites.services.UserService;
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
public class PrivateMessageController {

    private final PrivateMessageService privateMessageService;
    private final UserService userService;


    @PostMapping
    public ResponseEntity<PrivateMessageDTO> sendPrivateMessage(@Valid @RequestBody PrivateMessageCreateDTO privateMessageCreateDTO) {

        log.info("Received request to send private message");
        User user = getAuthenticatedUser();

        PrivateMessageDTO privateMessageDTO = privateMessageService.sendPrivateMessage(privateMessageCreateDTO, user);
        return ResponseEntity.ok(privateMessageDTO);
    }

    @GetMapping("/sent")
    public ResponseEntity<HttpResponse> getSentMessages(Pageable pageable) {

        log.info("Received request to get sent messages");
        User user = getAuthenticatedUser();
        Page<PrivateMessageDTO> sentMessages = privateMessageService.getSentMessages(user.getId(), pageable);
        return buildPageResponse(sentMessages, "Sent messages fetched successfully");
    }

    @GetMapping("/received")
    public ResponseEntity<HttpResponse> getReceivedMessages(Pageable pageable) {

        log.info("Received request to get received messages");
        User user = getAuthenticatedUser();
        Page<PrivateMessageDTO> receivedMessages = privateMessageService.getReceivedMessages(user.getId(), pageable);
        return buildPageResponse(receivedMessages, "Received messages fetched successfully");
    }

    @GetMapping("/{messageId}")
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


