package com.ratz.greenbites.services;

import com.ratz.greenbites.DTO.privateMessage.PrivateMessageCreateDTO;
import com.ratz.greenbites.DTO.privateMessage.PrivateMessageDTO;
import com.ratz.greenbites.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PrivateMessageService {

    PrivateMessageDTO sendPrivateMessage(PrivateMessageCreateDTO messageDTO, User sender);

    Page<PrivateMessageDTO> getReceivedMessages(Long userId, Pageable pageable);

    Page<PrivateMessageDTO> getSentMessages(Long userId, Pageable pageable);

    PrivateMessageDTO readMessage(Long messageId, Long userId);

    void deleteMessage(Long messageId, Long userId);

}
