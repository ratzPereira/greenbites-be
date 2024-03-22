package com.ratz.greenbites.DTO.profile;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProfilePhotosDTO {
    private List<MultipartFile> photoUrls;
}
