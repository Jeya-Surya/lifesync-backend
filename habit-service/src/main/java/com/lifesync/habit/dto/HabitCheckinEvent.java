package com.lifesync.habit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitCheckinEvent {

    private String userEmail;
    private String habitName;
    private Integer currentStreak;
    private Integer longestStreak;
    private LocalDate checkDate;
    private String eventType;
}
