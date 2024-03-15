package com.ratz.greenbites.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProfileUpdateDTO {

    private String firstName;
    private String lastName;
    private String bio;
    private String location;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    private List<String> photos;
    private String profileImageUrl;
}
