package com.ratz.greenbites.DTO.privateMessage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrivateMessageCreateDTO {

    @NotNull(message = "Recipient ID cannot be null")
    private Long recipientId;

    @NotBlank(message = "Subject cannot be blank")
    private String subject;

    @NotBlank(message = "Content cannot be blank")
    private String content;
}
