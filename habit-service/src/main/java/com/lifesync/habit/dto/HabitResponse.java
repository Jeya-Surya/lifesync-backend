package com.lifesync.habit.dto;

import com.lifesync.habit.model.HabitFrequency;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class HabitResponse {
    private Long id;
    private String name;
    private String description;
    private HabitFrequency frequency;
    private Integer currentStreak;
    private Integer longestStreak;
    private Boolean checkedInToday;
    private LocalDateTime createdAt;
}