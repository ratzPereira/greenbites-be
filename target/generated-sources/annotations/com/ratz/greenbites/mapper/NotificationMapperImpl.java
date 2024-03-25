package com.ratz.greenbites.mapper;

import com.ratz.greenbites.DTO.notification.NotificationDTO;
import com.ratz.greenbites.entity.Notification;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-25T22:04:09-0100",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 18.0.2 (Amazon.com Inc.)"
)
@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationDTO toDto(Notification notification) {
        if ( notification == null ) {
            return null;
        }

        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.setId( notification.getId() );
        if ( notification.getType() != null ) {
            notificationDTO.setType( notification.getType().name() );
        }
        notificationDTO.setContent( notification.getContent() );
        notificationDTO.setRead( notification.isRead() );
        notificationDTO.setCreatedAt( notification.getCreatedAt() );

        return notificationDTO;
    }
}
