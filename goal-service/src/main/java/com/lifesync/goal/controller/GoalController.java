package com.lifesync.goal.controller;

import com.lifesync.goal.dto.GoalRequest;
import com.lifesync.goal.dto.GoalResponse;
import com.lifesync.goal.dto.MilestoneRequest;
import com.lifesync.goal.model.Milestone;
import com.lifesync.goal.security.JwtUtil;
import com.lifesync.goal.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;
    private final JwtUtil jwtUtil;

    private String extractEmail(String authHeader) {
        String token = authHeader.substring(7);
        return jwtUtil.extractEmail(token);
    }

    @PostMapping
    public ResponseEntity<GoalResponse> createGoal(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody GoalRequest request) {

        String email = extractEmail(authHeader);
        return ResponseEntity.status(HttpStatus.CREATED).body(goalService.createGoal(email, request));
    }

    @GetMapping
    public ResponseEntity<List<GoalResponse>> getAllGoals(
            @RequestHeader("Authorization") String authHeader) {

        String email = extractEmail(authHeader);
        return ResponseEntity.ok().body(goalService.getAllGoals(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoalResponse> getGoalById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {

        String email = extractEmail(authHeader);
        return ResponseEntity.ok().body(goalService.getGoalById(email, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GoalResponse> updateGoal(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody GoalRequest request) {
        String email = extractEmail(authHeader);
        return ResponseEntity.ok(goalService.updateGoal(email, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        String email = extractEmail(authHeader);
        goalService.deleteGoal(email, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/milestones")
    public ResponseEntity<Milestone> addMilestone(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody MilestoneRequest request
            ) {

        String email = extractEmail(authHeader);
        return ResponseEntity.status(HttpStatus.CREATED).body(goalService.addMilestone(email, id, request));
    }

    @PatchMapping("{goalId}/milestones/{milestoneId}/complete")
    public ResponseEntity<Milestone> completeMilestone(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long goalId,
            @PathVariable Long milestoneId
    ) {
        String email = extractEmail(authHeader);
        return ResponseEntity.ok().body(goalService.completeMilestone(email, goalId, milestoneId));
    }

    @DeleteMapping("/{goalId}/milestones/{milestoneId}")
    public ResponseEntity<Void> deleteMilestone(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long goalId,
            @PathVariable Long milestoneId) {
        String email = extractEmail(authHeader);
        goalService.deleteMilestone(email, goalId, milestoneId);
        return ResponseEntity.noContent().build();
    }
}
