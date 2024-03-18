package com.ratz.greenbites.DTO.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProfileDTO {

    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    private String email;
    private String bio;
    private String location;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;

    private LocalDateTime createdAt;

    private List<String> photos;
    private String profileImageUrl;
}
