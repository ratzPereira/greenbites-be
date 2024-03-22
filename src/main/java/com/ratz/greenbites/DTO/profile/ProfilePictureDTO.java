package com.ratz.greenbites.DTO.profile;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfilePictureDTO {

    private MultipartFile profilePicture;
}
