package com.lifesync.jobtracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class JobApplicationRequest {

    @NotBlank(message = "Company name is required")
    private String companyName;

    @NotBlank(message = "Role is required")
    private String role;

    private LocalDate appliedDate;
    private LocalDate followUpDate;
    private String notes;
    private String jobUrl;
}
