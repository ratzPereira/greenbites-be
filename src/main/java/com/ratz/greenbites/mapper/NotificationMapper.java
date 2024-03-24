package com.ratz.greenbites.mapper;

import com.ratz.greenbites.DTO.notification.NotificationDTO;
import com.ratz.greenbites.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationMapper INSTANCE = Mappers.getMapper(NotificationMapper.class);

    NotificationDTO toDto(Notification notification);
}