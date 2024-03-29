package com.ratz.greenbites.services.Impl;

import com.ratz.greenbites.DTO.profile.ProfileUpdateDTO;
import com.ratz.greenbites.DTO.profile.RemovePhotosDTO;
import com.ratz.greenbites.entity.Profile;
import com.ratz.greenbites.exception.ApiException;
import com.ratz.greenbites.repository.ProfileRepository;
import com.ratz.greenbites.services.ProfileService;
import com.ratz.greenbites.services.external.AzureStorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final AzureStorageService azureStorageService;

    @Override
    public Profile getProfileByUserId(Long id) {

        log.debug("Fetching profile for user ID: {}", id);
        return profileRepository.getProfileByUserId(id).
                orElseThrow(() -> new ApiException("Profile not found for user" + id));
    }

    @Override
    @Transactional
    public Profile updateProfile(ProfileUpdateDTO profileDTO, Long userId) {

        log.info("Updating profile for user ID: {}", userId);

        Profile profile = profileRepository.getProfileByUserId(userId)
                .orElseThrow(() -> new ApiException("Profile not found for user" + userId));

        profile.setFirstName(profileDTO.getFirstName());
        profile.setLastName(profileDTO.getLastName());
        profile.setBio(profileDTO.getBio());
        profile.setBirthDate(profileDTO.getBirthDate());
        profile.setLocation(profileDTO.getLocation());

        return profileRepository.save(profile);
    }

    @Override
    @Transactional
    public Profile updateProfilePicture(MultipartFile profilePicture, Long userId) {
        log.info("Updating profile picture for user ID: {}", userId);

        Profile profile = profileRepository.getProfileByUserId(userId)
                .orElseThrow(() -> new ApiException("Profile not found for user" + userId));

        String photoUrl = azureStorageService.uploadFile(profilePicture);
        profile.setProfileImageUrl(photoUrl);

        return profileRepository.save(profile);
    }

    @Override
    @Transactional
    public Profile addPhotosToProfile(List<MultipartFile> photoUrls, Long userId) {
        log.info("Adding photos to profile for user ID: {}", userId);

        Profile profile = profileRepository.getProfileByUserId(userId)
                .orElseThrow(() -> new ApiException("Profile not found for user" + userId));

        List<String> urls = photoUrls.stream()
                .map(azureStorageService::uploadFile)
                .toList();
        profile.getPhotos().addAll(urls);

        return profileRepository.save(profile);
    }

    @Override
    @Transactional
    public Profile removePhotos(Long userId, RemovePhotosDTO dto) {
        log.info("Removing photos from profile for user ID: {}", userId);

        Profile profile = profileRepository.getProfileByUserId(userId)
                .orElseThrow(() -> new ApiException("Profile not found for user" + userId));

        if (dto.getProfilePictureUrl() != null && dto.getProfilePictureUrl().equals(profile.getProfileImageUrl())) {
            profile.setProfileImageUrl(null);
        }

        if (dto.getPhotoUrls() != null && !dto.getPhotoUrls().isEmpty()) {
            profile.getPhotos().removeAll(dto.getPhotoUrls());
        }

        return profileRepository.save(profile);
    }
}
