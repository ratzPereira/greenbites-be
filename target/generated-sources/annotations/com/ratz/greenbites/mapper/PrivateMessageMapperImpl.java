package com.ratz.greenbites.mapper;

import com.ratz.greenbites.DTO.privateMessage.PrivateMessageDTO;
import com.ratz.greenbites.entity.PrivateMessage;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-23T11:18:58-0100",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 18.0.2 (Amazon.com Inc.)"
)
@Component
public class PrivateMessageMapperImpl implements PrivateMessageMapper {

    @Override
    public PrivateMessageDTO privateMessageToPrivateMessageDTO(PrivateMessage privateMessage) {
        if ( privateMessage == null ) {
            return null;
        }

        PrivateMessageDTO privateMessageDTO = new PrivateMessageDTO();

        privateMessageDTO.setId( privateMessage.getId() );
        privateMessageDTO.setSubject( privateMessage.getSubject() );
        privateMessageDTO.setContent( privateMessage.getContent() );
        privateMessageDTO.setRead( privateMessage.isRead() );

        return privateMessageDTO;
    }

    @Override
    public PrivateMessage privateMessageDTOToPrivateMessage(PrivateMessageDTO privateMessageDTO) {
        if ( privateMessageDTO == null ) {
            return null;
        }

        PrivateMessage privateMessage = new PrivateMessage();

        privateMessage.setId( privateMessageDTO.getId() );
        privateMessage.setSubject( privateMessageDTO.getSubject() );
        privateMessage.setContent( privateMessageDTO.getContent() );

        return privateMessage;
    }
}
