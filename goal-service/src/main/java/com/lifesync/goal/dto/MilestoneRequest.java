package com.lifesync.goal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MilestoneRequest {

    @NotBlank(message = "Milestone title is required")
    private String title;

    private LocalDate dueDate;
}

