package com.lifesync.jobtracker.dto;

import com.lifesync.jobtracker.model.ApplicationStatus;
import com.lifesync.jobtracker.model.InterviewRound;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class JobApplicationResponse {
    private Long id;
    private String companyName;
    private String role;
    private ApplicationStatus status;
    private LocalDate appliedDate;
    private LocalDate followUpDate;
    private String notes;
    private String jobUrl;
    private LocalDate createdAt;
    private List<InterviewRound> rounds;
}