package com.ratz.greenbites.services.Impl;

import com.ratz.greenbites.entity.Notification;
import com.ratz.greenbites.entity.User;
import com.ratz.greenbites.enums.NotificationType;
import com.ratz.greenbites.repository.NotificationRepository;
import com.ratz.greenbites.repository.UserRepository;
import com.ratz.greenbites.services.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;


    @Override
    @Async
    public void createNotification(Long userId, NotificationType type, String content) {

        User recipient = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setType(type);
        notification.setContent(content);
        notification.setRead(false);
        notificationRepository.save(notification);
    }

    @Override
    public Page<Notification> getAllUserNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByRecipientId(userId, pageable);
    }

    @Override
    public Page<Notification> getUnreadUserNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByRecipientIdAndIsReadFalse(userId, pageable);
    }

    @Override
    public void markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void markAllNotificationsAsRead(Long userId) {

        List<Notification> notifications = notificationRepository.findByRecipientIdAndIsReadFalse(userId);
        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);
    }

}
