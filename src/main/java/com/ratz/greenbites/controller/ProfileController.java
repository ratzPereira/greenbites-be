package com.ratz.greenbites.controller;

import com.ratz.greenbites.DTO.ProfileDTO;
import com.ratz.greenbites.entity.Profile;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.mapper.ProfileResponseMapper;
import com.ratz.greenbites.services.ProfileService;
import com.ratz.greenbites.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/profile")
@Slf4j
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ProfileDTO> getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User user = userService.getUserByEmail(currentUsername);
        Profile profile = profileService.getProfileByUserId(user.getId());

        return buildProfileResponse(user, profile);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileDTO> getProfileByUserId(@PathVariable Long userId) {

        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Profile profile = profileService.getProfileByUserId(user.getId());
        return buildProfileResponse(user, profile);
    }

    private ResponseEntity<ProfileDTO> buildProfileResponse(User user, Profile profile) {

        ProfileDTO profileDTO = ProfileResponseMapper.INSTANCE.profileToProfileDTO(profile);

        profileDTO.setEmail(user.getEmail());
        profileDTO.setCreatedAt(user.getCreatedAt());

        return ResponseEntity.ok(profileDTO);
    }
}
