package com.ratz.greenbites.DTO.collection;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CreateCollectionDTO {
    @NotEmpty(message = "Collection name cannot be empty")
    private String name;
}
