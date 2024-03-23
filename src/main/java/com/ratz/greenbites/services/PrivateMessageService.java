package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.privateMessage.PrivateMessageCreateDTO;
import com.ratz.greenbites.DTO.privateMessage.PrivateMessageDTO;
import com.ratz.greenbites.entity.User;

public interface PrivateMessageService {

    PrivateMessageDTO sendPrivateMessage(PrivateMessageCreateDTO messageDTO, User sender);
}
