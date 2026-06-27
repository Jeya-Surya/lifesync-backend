package com.lifesync.jobtracker.controller;

import com.lifesync.jobtracker.dto.*;
import com.lifesync.jobtracker.model.InterviewRound;
import com.lifesync.jobtracker.security.JwtUtil;
import com.lifesync.jobtracker.service.JobTrackerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobTrackerController {

    private final JobTrackerService jobTrackerService;
    private final JwtUtil jwtUtil;

    private String extractEmail(String authHeader) {
        return jwtUtil.extractEmail(authHeader.substring(7));
    }


    @PostMapping
    public ResponseEntity<JobApplicationResponse> createApplication(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody JobApplicationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jobTrackerService.createApplication(
                        extractEmail(authHeader), request));
    }

    @GetMapping
    public ResponseEntity<List<JobApplicationResponse>> getAllApplications(
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(
                jobTrackerService.getAllApplications(extractEmail(authHeader)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> getApplicationById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        return ResponseEntity.ok(
                jobTrackerService.getApplicationById(
                        extractEmail(authHeader), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> updateApplication(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody JobApplicationRequest request) {
        return ResponseEntity.ok(
                jobTrackerService.updateApplication(
                        extractEmail(authHeader), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        jobTrackerService.deleteApplication(extractEmail(authHeader), id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<JobApplicationResponse> updateStatus(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(
                jobTrackerService.updateStatus(
                        extractEmail(authHeader), id, request));
    }


    @PostMapping("/{id}/rounds")
    public ResponseEntity<InterviewRound> addRound(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody InterviewRoundRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jobTrackerService.addRound(
                        extractEmail(authHeader), id, request));
    }

    @PutMapping("/{id}/rounds/{roundId}")
    public ResponseEntity<InterviewRound> updateRound(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @PathVariable Long roundId,
            @Valid @RequestBody InterviewRoundRequest request) {
        return ResponseEntity.ok(
                jobTrackerService.updateRound(
                        extractEmail(authHeader), id, roundId, request));
    }

    @GetMapping("/{id}/rounds")
    public ResponseEntity<List<InterviewRound>> getRounds(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        return ResponseEntity.ok(
                jobTrackerService.getRounds(extractEmail(authHeader), id));
    }


    @GetMapping("/stats")
    public ResponseEntity<JobStatsResponse> getStats(
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(
                jobTrackerService.getStats(extractEmail(authHeader)));
    }
}