package com.lifesync.notification.consumer;

import com.lifesync.notification.dto.JobStatusEvent;
import com.lifesync.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JobStatusConsumer {

    private final NotificationService notificationService;

    @KafkaListener(
            topics = "job-status-events",
            groupId = "notification-group",
            containerFactory = "jobKafkaListenerContainerFactory"
    )
    public void consumeJobStatus(JobStatusEvent event) {
        log.info("Received job status event: {} at {}",
                event.getNewStatus(), event.getCompanyName());

        String message = buildJobMessage(event);

        notificationService.saveNotification(
                event.getUserEmail(),
                event.getEventType(),
                message
        );
    }

    private String buildJobMessage(JobStatusEvent event) {
        if ("OFFER_RECEIVED".equals(event.getEventType())) {
            return String.format(
                    "🎉 Congratulations! You received an offer from %s " +
                            "for the %s position!",
                    event.getCompanyName(),
                    event.getRole()
            );
        }
        return String.format(
                "📋 Application update: Your application at %s for %s " +
                        "moved from %s to %s.",
                event.getCompanyName(),
                event.getRole(),
                event.getOldStatus(),
                event.getNewStatus()
        );
    }
}