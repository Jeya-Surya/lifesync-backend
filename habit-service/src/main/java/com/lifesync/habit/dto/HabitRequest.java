package com.lifesync.habit.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class HabitRequest {

    @NotBlank(message = "Habit name is required")
    private String name;

    private String description;

    private String frequency;
}
