package com.lifesync.jobtracker.service;

import com.lifesync.jobtracker.dto.JobStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobEventProducer {

    private final KafkaTemplate<String, JobStatusEvent> kafkaTemplate;

    private static final String TOPIC = "job-status-events";

    public void publishStatusEvent(JobStatusEvent event) {
        kafkaTemplate.send(TOPIC, event.getUserEmail(), event);
        log.info("Published job status event: {} -> {} for {}", event.getOldStatus(), event.getNewStatus(), event.getCompanyName());
    }
}
