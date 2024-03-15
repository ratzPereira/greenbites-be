package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.ProfileUpdateDTO;
import com.ratz.greenbites.entity.Profile;
import com.ratz.greenbites.entity.User;

public interface ProfileService {

    Profile getProfileByUserId(Long id);

    Profile updateProfile(ProfileUpdateDTO profileDTO, User user);
}
