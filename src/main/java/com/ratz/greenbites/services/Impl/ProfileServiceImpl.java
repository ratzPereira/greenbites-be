package com.ratz.greenbites.services.Impl;

import com.ratz.greenbites.DTO.ProfileUpdateDTO;
import com.ratz.greenbites.entity.Profile;
import com.ratz.greenbites.exception.ApiException;
import com.ratz.greenbites.repository.ProfileRepository;
import com.ratz.greenbites.repository.UserRepository;
import com.ratz.greenbites.services.ProfileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

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
    public Profile updateProfilePicture(String photoUrl, Long userId) {
        log.info("Updating profile picture for user ID: {}", userId);

        Profile profile = profileRepository.getProfileByUserId(userId)
                .orElseThrow(() -> new ApiException("Profile not found for user" + userId));

        profile.setProfileImageUrl(photoUrl);

        return profileRepository.save(profile);
    }

    @Override
    @Transactional
    public Profile addPhotosToProfile(List<String> photoUrl, Long userId) {
        log.info("Adding photos to profile for user ID: {}", userId);

        Profile profile = profileRepository.getProfileByUserId(userId)
                .orElseThrow(() -> new ApiException("Profile not found for user" + userId));

        profile.getPhotos().addAll(photoUrl);

        return profileRepository.save(profile);
    }
}
