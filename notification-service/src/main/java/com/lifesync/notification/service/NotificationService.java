package com.lifesync.notification.service;

import com.lifesync.notification.model.Notification;
import com.lifesync.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void saveNotification(String userEmail, String type, String message) {
        Notification notification = Notification.builder()
                .userEmail(userEmail)
                .type(type)
                .message(message)
                .build();

        notificationRepository.save(notification);
        log.info("Saved {} notification for {}", type, userEmail);
    }

    public List<Notification> getAllNotifications(String userEmail) {
        return notificationRepository.findByUserEmailOrderByCreatedAtDesc(userEmail);
    }

    public List<Notification> getUnreadNotifications(String userEmail) {
        return notificationRepository.findByUserEmailAndIsReadFalse(userEmail);
    }

    public long getUnreadCount(String userEmail) {
        return notificationRepository.countByUserEmailAndIsReadFalse(userEmail);
    }

    public void markAllAsRead(String userEmail) {
        List<Notification> unread =  notificationRepository.findByUserEmailAndIsReadFalse(userEmail);

        unread.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(unread);
    }
}
