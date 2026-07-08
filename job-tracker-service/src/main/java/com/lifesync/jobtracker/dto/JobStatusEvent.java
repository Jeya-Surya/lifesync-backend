package com.lifesync.jobtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
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
