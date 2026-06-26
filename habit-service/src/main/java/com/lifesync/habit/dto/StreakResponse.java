package com.lifesync.habit.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StreakResponse {

    private Long habitId;
    private String habitName;
    private Integer currentStreak;
    private Integer longestStreak;
    private Boolean checkedInToday;
}
