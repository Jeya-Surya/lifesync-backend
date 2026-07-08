package com.lifesync.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobStatusEvent {
    private String userEmail;
    private String companyName;
    private String role;
    private String oldStatus;
    private String newStatus;
    private String eventType;
}