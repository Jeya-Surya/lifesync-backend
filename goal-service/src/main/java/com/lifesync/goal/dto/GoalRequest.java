package com.lifesync.goal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GoalRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    private LocalDate targetDate;
}
