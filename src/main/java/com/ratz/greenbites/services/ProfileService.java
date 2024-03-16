package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.ProfileUpdateDTO;
import com.ratz.greenbites.DTO.RemovePhotosDTO;
import com.ratz.greenbites.entity.Profile;

import java.util.List;

public interface ProfileService {

    Profile getProfileByUserId(Long id);

    Profile updateProfile(ProfileUpdateDTO profileDTO, Long id);

    Profile updateProfilePicture(String photoUrl, Long id);

    Profile addPhotosToProfile(List<String> photoUrl, Long id);

    Profile removePhotos(Long userId, RemovePhotosDTO dto);
}
