package com.ratz.greenbites.DTO.profile;

import lombok.Data;

import java.util.List;

@Data
public class RemovePhotosDTO {

    private String profilePictureUrl;
    private List<String> photoUrls;
}
