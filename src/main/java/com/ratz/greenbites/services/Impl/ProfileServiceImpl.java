package com.ratz.greenbites.services.Impl;

import com.ratz.greenbites.DTO.ProfileUpdateDTO;
import com.ratz.greenbites.entity.Profile;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.exception.ApiException;
import com.ratz.greenbites.repository.ProfileRepository;
import com.ratz.greenbites.repository.UserRepository;
import com.ratz.greenbites.services.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Override
    public Profile getProfileByUserId(Long id) {

        log.debug("Fetching profile for user ID: {}", id);
        return profileRepository.getProfileByUserId(id);
    }

    @Override
    public Profile updateProfile(ProfileUpdateDTO profileDTO, User user) {

        log.info("Updating profile for user ID: {}", user.getId());

        Profile profile = profileRepository.getProfileByUserId(user.getId());

        if (profile == null) {
            throw new ApiException("Profile not found for user with id: " + user.getId());
        }

        profile.setFirstName(profileDTO.getFirstName());
        profile.setLastName(profileDTO.getLastName());
        profile.setBio(profileDTO.getBio());
        profile.setBirthDate(profileDTO.getBirthDate());
        profile.setLocation(profileDTO.getLocation());

        return profileRepository.save(profile);
    }
}
