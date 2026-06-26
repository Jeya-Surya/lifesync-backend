package com.lifesync.goal.service;

import com.lifesync.goal.dto.GoalRequest;
import com.lifesync.goal.dto.GoalResponse;
import com.lifesync.goal.dto.MilestoneRequest;
import com.lifesync.goal.model.Goal;
import com.lifesync.goal.model.Milestone;
import com.lifesync.goal.repository.GoalRepository;
import com.lifesync.goal.repository.MilestoneRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final MilestoneRepository milestoneRepository;

    public GoalResponse createGoal(String userEmail, GoalRequest request) {

        Goal goal = Goal.builder()
                .userEmail(userEmail)
                .title(request.getTitle())
                .description(request.getDescription())
                .targetDate(request.getTargetDate())
                .build();

        Goal saved = goalRepository.save(goal);
        return mapToResponse(saved);
    }

    public List<GoalResponse> getAllGoals(String userEmail) {
        return goalRepository
                .findByUserEmailOrderByCreatedAtDesc(userEmail)
                .stream()
                .map(this::mapToResponse)
                .toList();

    }

    public GoalResponse getGoalById(String userEmail, Long goalId) {
        Goal goal = goalRepository.findByIdAndUserEmail(goalId, userEmail)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        return mapToResponse(goal);
    }

    public GoalResponse updateGoal(String userEmail, Long goalId, GoalRequest request) {
        Goal goal = goalRepository.findByIdAndUserEmail(goalId, userEmail)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        goal.setTitle(request.getTitle());
        goal.setDescription(request.getDescription());
        goal.setTargetDate(request.getTargetDate());

        return mapToResponse(goal);
    }

    public void deleteGoal(String userEmail, Long goalId) {
        Goal goal = goalRepository.findByIdAndUserEmail(goalId, userEmail)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        goalRepository.delete(goal);
    }

    public Milestone addMilestone(String userEmail, Long goalId, MilestoneRequest request) {
        Goal goal = goalRepository.findByIdAndUserEmail(goalId, userEmail)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        Milestone milestone = Milestone.builder()
                .tile(request.getTitle())
                .dueDate(request.getDueDate())
                .goal(goal)
                .build();

        Milestone saved = milestoneRepository.save(milestone);
        recalculateProgress(goal);
        return saved;
    }

    public Milestone completeMilestone(String userEmail, Long goalId, Long milestoneId) {
        goalRepository.findByIdAndUserEmail(goalId, userEmail)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        Milestone milestone = milestoneRepository.findByIdAndGoalUserEmail(milestoneId, userEmail)
                .orElseThrow(() -> new RuntimeException("Milestone not found"));

        milestone.setIsCompleted(true);
        Milestone saved = milestoneRepository.save(milestone);

        recalculateProgress(milestone.getGoal());
        return saved;
    }

    public void deleteMilestone(String userEmail, Long goalId,
                                Long milestoneId) {
        goalRepository.findByIdAndUserEmail(goalId, userEmail)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        Milestone milestone = milestoneRepository
                .findByIdAndGoalUserEmail(milestoneId, userEmail)
                .orElseThrow(() -> new RuntimeException("Milestone not found"));

        milestoneRepository.delete(milestone);
    }

    @Transactional
    protected void recalculateProgress(Goal goal) {

        List<Milestone> milestones = goal.getMilestones();

        if (milestones.isEmpty()) {
            goal.setProgressPercent(0);
        } else {
            long completed = milestones.stream()
                    .filter(Milestone::getIsCompleted)
                    .count();

            int percent = (int) ((completed * 100) / milestones.size());
            goal.setProgressPercent(percent);
        }
        goalRepository.save(goal);
    }

    private GoalResponse mapToResponse(Goal goal) {
        return GoalResponse.builder()
                .id(goal.getId())
                .id(goal.getId())
                .title(goal.getTitle())
                .description(goal.getDescription())
                .status(goal.getStatus())
                .targetDate(goal.getTargetDate())
                .progressPercent(goal.getProgressPercent())
                .createdAt(goal.getCreatedAt())
                .milestones(goal.getMilestones())
                .build();
    }
}
