package com.ratz.greenbites.services;

import com.ratz.greenbites.entity.Notification;
import com.ratz.greenbites.enums.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

    void createNotification(Long userId, NotificationType type, String content);

    Page<Notification> getAllUserNotifications(Long userId, Pageable pageable);

    Page<Notification> getUnreadUserNotifications(Long userId, Pageable pageable);

    void markNotificationAsRead(Long notificationId);

    void markAllNotificationsAsRead(Long userId);


}
