package com.ratz.greenbites.DTO.auth;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Long id;
    private String email;
    private boolean enabled;
    private boolean notLocked;
    private boolean usingMfa;
    private LocalDateTime createdAt;
}
