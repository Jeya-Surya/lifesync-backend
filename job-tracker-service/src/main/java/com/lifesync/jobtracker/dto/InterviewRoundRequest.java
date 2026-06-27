package com.lifesync.jobtracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InterviewRoundRequest {

    @NotBlank(message = "Round name is required")
    private String roundName;

    private LocalDate roundDate;
    private String notes;
    private String outcome;
}
