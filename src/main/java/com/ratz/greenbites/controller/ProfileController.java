package com.ratz.greenbites.controller;

import com.ratz.greenbites.DTO.profile.ProfileDTO;
import com.ratz.greenbites.DTO.profile.ProfileUpdateDTO;
import com.ratz.greenbites.DTO.profile.RemovePhotosDTO;
import com.ratz.greenbites.entity.Profile;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.mapper.ProfileResponseMapper;
import com.ratz.greenbites.services.ProfileService;
import com.ratz.greenbites.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Profile", description = "The Profile API for managing user profile.")
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get Current User Profile", description = "Fetches the profile information of the current logged-in user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile information retrieved successfully", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Profile not found")})
    public ResponseEntity<ProfileDTO> getCurrentUserProfile() {

        User user = getAuthenticatedUser();

        Profile profile = profileService.getProfileByUserId(user.getId());

        return buildProfileResponse(user, profile);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get Profile by User ID", description = "Fetches the profile information of a specific user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile information retrieved successfully", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "User or profile not found")})
    public ResponseEntity<ProfileDTO> getProfileByUserId(@PathVariable Long userId) {

        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Profile profile = profileService.getProfileByUserId(user.getId());
        return buildProfileResponse(user, profile);
    }

    @PutMapping
    @Operation(summary = "Update Profile", description = "Updates the current user's profile information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid profile information provided")})
    public ResponseEntity<ProfileDTO> updateProfile(@RequestBody ProfileUpdateDTO profileDTO) {

        User user = getAuthenticatedUser();

        Profile profile = profileService.updateProfile(profileDTO, user.getId());

        return buildProfileResponse(user, profile);
    }

    @PutMapping("/photo")
    @Operation(summary = "Update Profile Picture", description = "Updates the profile picture of the current user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile picture updated successfully", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid or empty file provided")})
    public ResponseEntity<ProfileDTO> updateProfilePicture(@RequestParam("profilePicture") MultipartFile profilePicture) {
        if (profilePicture.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        User user = getAuthenticatedUser();
        Profile profile = profileService.updateProfilePicture(profilePicture, user.getId());

        return buildProfileResponse(user, profile);
    }

    @PostMapping("/photos")
    @Operation(summary = "Add Photos to Profile", description = "Adds multiple photos to the current user's profile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Photos added to profile successfully", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid or empty files provided")})
    public ResponseEntity<ProfileDTO> addPhotos(@RequestParam("photoUrls") List<MultipartFile> photoUrls) {
        if (photoUrls.stream().anyMatch(MultipartFile::isEmpty)) {
            return ResponseEntity.badRequest().build();
        }

        User user = getAuthenticatedUser();
        Profile profile = profileService.addPhotosToProfile(photoUrls, user.getId());

        return buildProfileResponse(user, profile);
    }

    @DeleteMapping("/photos")
    @Operation(summary = "Remove Photos from Profile", description = "Removes specified photos from the current user's profile.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Photos removed successfully", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid request")})
    public ResponseEntity<?> removePhotos(@RequestBody RemovePhotosDTO dto) {

        User user = getAuthenticatedUser();

        profileService.removePhotos(user.getId(), dto);

        return ResponseEntity.ok().build();
    }


    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return userService.getUserByEmail(currentUsername);
    }

    private ResponseEntity<ProfileDTO> buildProfileResponse(User user, Profile profile) {

        ProfileDTO profileDTO = ProfileResponseMapper.INSTANCE.profileToProfileDTO(profile);

        profileDTO.setEmail(user.getEmail());
        profileDTO.setCreatedAt(user.getCreatedAt());

        return ResponseEntity.ok(profileDTO);
    }
}
