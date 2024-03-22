package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.profile.ProfileUpdateDTO;
import com.ratz.greenbites.DTO.profile.RemovePhotosDTO;
import com.ratz.greenbites.entity.Profile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProfileService {

    Profile getProfileByUserId(Long id);

    Profile updateProfile(ProfileUpdateDTO profileDTO, Long id);

    Profile updateProfilePicture(MultipartFile photo, Long id);

    Profile addPhotosToProfile(List<MultipartFile> photos, Long id);

    Profile removePhotos(Long userId, RemovePhotosDTO dto);
}
