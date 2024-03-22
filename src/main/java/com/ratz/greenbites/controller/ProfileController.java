package com.ratz.greenbites.controller;

import com.ratz.greenbites.DTO.profile.ProfileDTO;
import com.ratz.greenbites.DTO.profile.ProfileUpdateDTO;
import com.ratz.greenbites.DTO.profile.RemovePhotosDTO;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user/profile")
@Slf4j
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ProfileDTO> getCurrentUserProfile() {

        User user = getAuthenticatedUser();

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

    @PutMapping
    public ResponseEntity<ProfileDTO> updateProfile(@RequestBody ProfileUpdateDTO profileDTO) {

        User user = getAuthenticatedUser();

        Profile profile = profileService.updateProfile(profileDTO, user.getId());

        return buildProfileResponse(user, profile);
    }

    @PutMapping("/photo")
    public ResponseEntity<ProfileDTO> updateProfilePicture(@RequestParam("profilePicture") MultipartFile profilePicture) {
        if (profilePicture.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        User user = getAuthenticatedUser();
        Profile profile = profileService.updateProfilePicture(profilePicture, user.getId());

        return buildProfileResponse(user, profile);
    }

    @PostMapping("/photos")
    public ResponseEntity<ProfileDTO> addPhotos(@RequestParam("photoUrls") List<MultipartFile> photoUrls) {
        if (photoUrls.stream().anyMatch(MultipartFile::isEmpty)) {
            return ResponseEntity.badRequest().build();
        }

        User user = getAuthenticatedUser();
        Profile profile = profileService.addPhotosToProfile(photoUrls, user.getId());

        return buildProfileResponse(user, profile);
    }

    @DeleteMapping("/photos")
    public ResponseEntity<?> removePhotos(@RequestBody RemovePhotosDTO dto) {

        User user = getAuthenticatedUser();

        profileService.removePhotos(user.getId(), dto);

        return ResponseEntity.ok().build();
    }


    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // ou getEmail(), dependendo de como você configurou UserDetails
        return userService.getUserByEmail(currentUsername);
    }

    private ResponseEntity<ProfileDTO> buildProfileResponse(User user, Profile profile) {

        ProfileDTO profileDTO = ProfileResponseMapper.INSTANCE.profileToProfileDTO(profile);

        profileDTO.setEmail(user.getEmail());
        profileDTO.setCreatedAt(user.getCreatedAt());

        return ResponseEntity.ok(profileDTO);
    }
}
