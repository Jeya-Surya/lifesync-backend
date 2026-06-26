package com.lifesync.goal.dto;

import com.lifesync.goal.model.GoalStatus;
import com.lifesync.goal.model.Milestone;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class GoalResponse {

    private Long id;
    private String title;
    private String description;
    private GoalStatus status;
    private LocalDate targetDate;
    private Integer progressPercent;
    private LocalDateTime createdAt;
    private List<Milestone> milestones;
}
