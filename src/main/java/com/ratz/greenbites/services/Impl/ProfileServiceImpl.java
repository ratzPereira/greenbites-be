package com.ratz.greenbites.services.Impl;

import com.ratz.greenbites.DTO.ProfileDTO;
import com.ratz.greenbites.entity.Profile;
import com.ratz.greenbites.entity.User;
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
    public Profile createProfile(ProfileDTO profileDTO, User user) {
        return null;
    }

    @Override
    public Profile updateProfile(ProfileDTO profileDTO, User user) {
        return null;
    }
}
