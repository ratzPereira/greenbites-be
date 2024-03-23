package com.ratz.greenbites.mapper;

import com.ratz.greenbites.DTO.privateMessage.PrivateMessageDTO;
import com.ratz.greenbites.entity.PrivateMessage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PrivateMessageMapper {

    PrivateMessageMapper INSTANCE = Mappers.getMapper(PrivateMessageMapper.class);

    PrivateMessageDTO privateMessageToPrivateMessageDTO(PrivateMessage privateMessage);

    PrivateMessage privateMessageDTOToPrivateMessage(PrivateMessageDTO privateMessageDTO);
}
