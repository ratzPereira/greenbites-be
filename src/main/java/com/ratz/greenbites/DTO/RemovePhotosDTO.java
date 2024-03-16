package com.ratz.greenbites.DTO;

import lombok.Data;

import java.util.List;

@Data
public class RemovePhotosDTO {

    private String profilePictureUrl;
    private List<String> photoUrls;
}
