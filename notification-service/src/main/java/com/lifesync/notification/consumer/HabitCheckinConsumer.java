package com.lifesync.notification.consumer;

import com.lifesync.notification.dto.HabitCheckinEvent;
import com.lifesync.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class HabitCheckinConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "habit-checkin-events",
            groupId = "notification-group",
            containerFactory = "habitKafkaListenerContainerFactory"
    )
    public void consumeHabitCheckin(HabitCheckinEvent event) {
        log.info("Received habit checkin event: {} - streak {}", event.getHabitName(), event.getCurrentStreak());

        String message = buildMessage(event);

        notificationService.saveNotification(event.getUserEmail(), event.getEventType(), message);
    }

    private String buildMessage(HabitCheckinEvent event) {
        if ("STREAK_MILESTONE".equals(event.getEventType())) {
            return String.format(
                    "🔥 Milestone! You've maintained your '%s' habit " +
                            "for %d days! Keep it up!",
                    event.getHabitName(),
                    event.getCurrentStreak()
            );
        }
        return String.format(
                "✅ Great job! You checked in for '%s'. " +
                        "Current streak: %d days.",
                event.getHabitName(),
                event.getCurrentStreak()
        );
    }
}
