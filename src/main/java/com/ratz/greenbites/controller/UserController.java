package com.ratz.greenbites.controller;

import com.ratz.greenbites.DTO.auth.LoginFormDTO;
import com.ratz.greenbites.DTO.auth.RegisterFormDTO;
import com.ratz.greenbites.DTO.auth.UserDTO;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.entity.UserPrincipal;
import com.ratz.greenbites.mapper.UserMapper;
import com.ratz.greenbites.provider.TokenProvider;
import com.ratz.greenbites.response.HttpResponse;
import com.ratz.greenbites.services.RoleService;
import com.ratz.greenbites.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Map;

import static java.time.LocalDateTime.now;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> createPlayer(@RequestBody @Valid RegisterFormDTO registerFormDTO) {

        log.info("Attempting to register a new player with email: {}", registerFormDTO.getEmail());
        UserDTO userDTO = userService.createUser(registerFormDTO);

        return ResponseEntity.created(getURI())
                .body(HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("user", userDTO))
                        .message("User created")
                        .statusCode(HttpStatus.CREATED.value())
                        .httpStatus(HttpStatus.CREATED)
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody @Valid LoginFormDTO loginForm) {

        log.info("Attempting to login user with email: {}", loginForm.getEmail());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword()));
        User user = userService.getUserByEmail(loginForm.getEmail());

        return sendResponse(UserMapper.INSTANCE.userToUserDTO(user));
    }


    private URI getURI() {
        return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/user/get/<userId>").toUriString());
    }

    private UserPrincipal getUserPrincipal(UserDTO userDTO) {
        return new UserPrincipal((userService.getUserByEmail(userDTO.getEmail())), roleService.getRoleByPlayerId(userDTO.getId()).getPermission());
    }

    private ResponseEntity<HttpResponse> sendResponse(UserDTO userDTO) {

        return ResponseEntity.ok()
                .body(HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("user", userDTO,
                                "access_token", tokenProvider.createAccessToken(getUserPrincipal(userDTO)),
                                "refresh_token", tokenProvider.createRefreshToken(getUserPrincipal(userDTO))))
                        .message("User logged in")
                        .statusCode(HttpStatus.OK.value())
                        .httpStatus(HttpStatus.OK)
                        .build());
    }
}
