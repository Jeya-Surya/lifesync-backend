package com.lifesync.aicoach.controller;

import com.lifesync.aicoach.dto.BriefingResponse;
import com.lifesync.aicoach.security.JwtUtil;
import com.lifesync.aicoach.service.AiCoachService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coach")
@RequiredArgsConstructor
public class AiCoachController {

    private final AiCoachService aiCoachService;
    private final JwtUtil jwtUtil;

    private String extractEmail(String authHeader) {
        return jwtUtil.extractEmail(authHeader.substring(7));
    }

    @GetMapping("/briefing")
    public ResponseEntity<BriefingResponse> getBriefing(
            @RequestHeader("Authorization") String authHeader) {

        String email = extractEmail(authHeader);
        BriefingResponse briefing =
                aiCoachService.generateBriefing(email, authHeader);
        return ResponseEntity.ok(briefing);
    }
}