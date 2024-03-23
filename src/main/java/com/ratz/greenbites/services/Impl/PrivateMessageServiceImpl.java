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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PrivateMessageServiceImpl implements PrivateMessageService {

    private final PrivateMessageRepository privateMessageRepository;
    private final UserRepository userRepository;
    private final PrivateMessageMapper privateMessageMapper;


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

    @Override
    public Page<PrivateMessageDTO> getSentMessages(Long userId, Pageable pageable) {
        return privateMessageRepository.findBySenderId(userId, pageable)
                .map(privateMessageMapper::privateMessageToPrivateMessageDTO);
    }

    @Override
    public Page<PrivateMessageDTO> getReceivedMessages(Long userId, Pageable pageable) {
        return privateMessageRepository.findByRecipientId(userId, pageable)
                .map(privateMessageMapper::privateMessageToPrivateMessageDTO);
    }

    @Override
    public PrivateMessageDTO readMessage(Long messageId, Long userId) {
        PrivateMessage message = privateMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        if (!message.getRecipient().getId().equals(userId)) {
            throw new RuntimeException("You do not have permission to read this message");
        }
        message.setRead(true);
        privateMessageRepository.save(message);
        return privateMessageMapper.privateMessageToPrivateMessageDTO(message);
    }

    @Override
    public void deleteMessage(Long messageId, Long userId) {
        PrivateMessage message = privateMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        if (!message.getSender().getId().equals(userId) && !message.getRecipient().getId().equals(userId)) {
            throw new RuntimeException("You do not have permission to delete this message");
        }
        privateMessageRepository.deleteById(messageId);
    }
}
