package com.lifesync.notification.controller;

import com.lifesync.notification.model.Notification;
import com.lifesync.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications(
            @RequestParam String userEmail) {
        return ResponseEntity.ok(
                notificationService.getAllNotifications(userEmail));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnread(
            @RequestParam String userEmail) {
        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(userEmail));
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @RequestParam String userEmail) {
        long count = notificationService.getUnreadCount(userEmail);
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(
            @RequestParam String userEmail) {
        notificationService.markAllAsRead(userEmail);
        return ResponseEntity.ok().build();
    }
}
