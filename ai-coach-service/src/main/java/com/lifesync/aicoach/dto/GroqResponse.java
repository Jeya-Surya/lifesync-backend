package com.lifesync.aicoach.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.jsonwebtoken.security.Message;
import lombok.Data;

import java.awt.*;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroqResponse {

    private List<Choice> choices;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private Message message;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        private String content;
    }
}
