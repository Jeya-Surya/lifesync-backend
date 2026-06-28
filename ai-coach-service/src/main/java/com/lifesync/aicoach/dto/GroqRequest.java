package com.lifesync.aicoach.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GroqRequest {

    private String model;
    private List<Message> messages;
    private int max_tokens;
    private double temperature;

    @Data
    @Builder
    public static class Message {
        private String role;
        private String content;
    }
}
