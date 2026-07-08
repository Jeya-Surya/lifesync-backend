package com.lifesync.habit.service;

import com.lifesync.habit.dto.HabitCheckinEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class HabitEventProducer {

    private final KafkaTemplate<String, HabitCheckinEvent> kafkaTemplate;

    private static final String TOPIC = "habit-checkin-events";

    public void publishCheckinEvent(HabitCheckinEvent event) {
        kafkaTemplate.send(TOPIC, event.getUserEmail(), event);
        log.info("Published habit checkin event for user: {}", event.getUserEmail());
    }
}
