package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.ProfileDTO;
import com.ratz.greenbites.entity.Profile;
import com.ratz.greenbites.entity.User;

public interface ProfileService {

    Profile getProfileByUserId(Long id);

    Profile createProfile(ProfileDTO profileDTO, User user);

    Profile updateProfile(ProfileDTO profileDTO, User user);
}
