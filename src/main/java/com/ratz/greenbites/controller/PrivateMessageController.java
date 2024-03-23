package com.ratz.greenbites.controller;

import com.ratz.greenbites.DTO.privateMessage.PrivateMessageCreateDTO;
import com.ratz.greenbites.DTO.privateMessage.PrivateMessageDTO;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.services.PrivateMessageService;
import com.ratz.greenbites.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userService.getUserByEmail(currentUsername);
    }

}


