package com.lifesync.habit.controller;

import com.lifesync.habit.dto.HabitRequest;
import com.lifesync.habit.dto.HabitResponse;
import com.lifesync.habit.dto.StreakResponse;
import com.lifesync.habit.model.HabitLog;
import com.lifesync.habit.security.JwtUtil;
import com.lifesync.habit.service.HabitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;
    private final JwtUtil jwtUtil;

    private String extractEmail(String authHeader) {
        return jwtUtil.extractEmail(authHeader.substring(7));
    }

    @PostMapping
    public ResponseEntity<HabitResponse> createHabit(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody HabitRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(habitService.createHabit(extractEmail(authHeader), request));
    }

    @GetMapping
    public ResponseEntity<List<HabitResponse>> getAllHabits(
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(
                habitService.getAllHabits(extractEmail(authHeader)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitResponse> getHabitById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        return ResponseEntity.ok(
                habitService.getHabitById(extractEmail(authHeader), id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabit(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        habitService.deleteHabit(extractEmail(authHeader), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/checkin")
    public ResponseEntity<HabitResponse> checkIn(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        return ResponseEntity.ok(
                habitService.checkIn(extractEmail(authHeader), id));
    }

    @GetMapping("/{id}/streak")
    public ResponseEntity<StreakResponse> getStreak(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        return ResponseEntity.ok(
                habitService.getStreak(extractEmail(authHeader), id));
    }

    @GetMapping("/{id}/logs")
    public ResponseEntity<List<HabitLog>> getLogs(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        return ResponseEntity.ok(
                habitService.getLogs(extractEmail(authHeader), id));
    }
}