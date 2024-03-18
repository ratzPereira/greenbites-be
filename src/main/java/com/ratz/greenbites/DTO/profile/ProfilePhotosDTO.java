package com.ratz.greenbites.DTO.profile;

import lombok.Data;

import java.util.List;

@Data
public class ProfilePhotosDTO {
    private List<String> photoUrls;
}
