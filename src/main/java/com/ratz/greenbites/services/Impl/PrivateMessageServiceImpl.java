package com.ratz.greenbites.services.Impl;

import com.ratz.greenbites.DTO.privateMessage.PrivateMessageCreateDTO;
import com.ratz.greenbites.DTO.privateMessage.PrivateMessageDTO;
import com.ratz.greenbites.entity.PrivateMessage;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.mapper.PrivateMessageMapper;
import com.ratz.greenbites.repository.PrivateMessageRepository;
import com.ratz.greenbites.repository.UserRepository;
import com.ratz.greenbites.services.PrivateMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PrivateMessageServiceImpl implements PrivateMessageService {

    private final PrivateMessageRepository privateMessageRepository;
    private final UserRepository userRepository;


    @Override
    public PrivateMessageDTO sendPrivateMessage(PrivateMessageCreateDTO messageDTO, User sender) {

        User recipient = userRepository.findById(messageDTO.getRecipientId())
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        PrivateMessage message = new PrivateMessage();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setSubject(messageDTO.getSubject());
        message.setContent(messageDTO.getContent());
        message = privateMessageRepository.save(message);

        return PrivateMessageMapper.INSTANCE.privateMessageToPrivateMessageDTO(message);
    }
}
