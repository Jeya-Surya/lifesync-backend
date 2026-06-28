package com.lifesync.aicoach.service;

import com.lifesync.aicoach.dto.BriefingResponse;
import com.lifesync.aicoach.dto.GroqRequest;
import com.lifesync.aicoach.dto.GroqResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiCoachService {

    // Load-balanced client for internal service calls
    private final WebClient.Builder webClientBuilder;

    // Direct client for Groq API
    @Qualifier("groqWebClient")
    private final WebClient groqWebClient;

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.url}")
    private String groqApiUrl;

    @Value("${groq.model}")
    private String groqModel;

    @Value("${services.goal-service.url}")
    private String goalServiceUrl;

    @Value("${services.habit-service.url}")
    private String habitServiceUrl;

    @Value("${services.job-service.url}")
    private String jobServiceUrl;

    // ── MAIN METHOD: Generate morning briefing ────────────────

    public BriefingResponse generateBriefing(String userEmail,
                                             String authHeader) {
        // Step 1: Fetch data from all 3 services in parallel
        String goalsData = fetchGoals(authHeader);
        String habitsData = fetchHabits(authHeader);
        String jobsData = fetchJobStats(authHeader);

        // Step 2: Build a rich prompt with all the data
        String prompt = buildPrompt(userEmail, goalsData,
                habitsData, jobsData);

        // Step 3: Send to Groq and get AI response
        String aiResponse = callGroq(prompt);

        // Step 4: Parse AI response into structured briefing
        return parseBriefing(aiResponse);
    }

    // ── STEP 1: Fetch from internal services ─────────────────

    private String fetchGoals(String authHeader) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(goalServiceUrl + "/api/goals")
                    .header("Authorization", authHeader)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();   // block() converts reactive to synchronous
        } catch (Exception e) {
            log.warn("Could not fetch goals: {}", e.getMessage());
            return "[]";        // return empty array if service is down
        }
    }

    private String fetchHabits(String authHeader) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(habitServiceUrl + "/api/habits")
                    .header("Authorization", authHeader)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.warn("Could not fetch habits: {}", e.getMessage());
            return "[]";
        }
    }

    private String fetchJobStats(String authHeader) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(jobServiceUrl + "/api/jobs/stats")
                    .header("Authorization", authHeader)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.warn("Could not fetch job stats: {}", e.getMessage());
            return "{}";
        }
    }

    // ── STEP 2: Build the AI prompt ───────────────────────────

    private String buildPrompt(String userEmail, String goals,
                               String habits, String jobStats) {
        String timeOfDay;
        int hour = LocalTime.now().getHour();
        if (hour < 12) timeOfDay = "morning";
        else if (hour < 17) timeOfDay = "afternoon";
        else timeOfDay = "evening";

        return String.format("""
            You are LifeSync AI Coach — a personal productivity coach
            for a student actively looking
            for a job.

            Today is %s.
            
            Tell the user "Good %s" using the time of day.

            Here is the user's current data:

            GOALS:
            %s

            HABITS (with streak info):
            %s

            JOB APPLICATION STATS:
            %s

            Based on this data, generate a personalized morning briefing.
            Be specific, motivating, and actionable.

            Respond in this EXACT format (keep the labels):
            GREETING: [a short personalized good morning (or depends on what time it is) message]
            SUMMARY: [2-3 sentence overview of their current status]
            INSIGHT_1: [specific observation about their goals]
            INSIGHT_2: [specific observation about their habits or streaks]
            INSIGHT_3: [specific observation about their job search]
            FOCUS: [the single most important thing they should do today]
            """,
                LocalDate.now(), timeOfDay, goals, habits, jobStats
        );
    }

    // ── STEP 3: Call Groq API ─────────────────────────────────

    private String callGroq(String prompt) {
        GroqRequest request = GroqRequest.builder()
                .model(groqModel)
                .max_tokens(500)
                .temperature(0.7)
                .messages(List.of(
                        GroqRequest.Message.builder()
                                .role("user")
                                .content(prompt)
                                .build()
                ))
                .build();

        GroqResponse response = groqWebClient
                .post()
                .uri(groqApiUrl)
                .header("Authorization", "Bearer " + groqApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GroqResponse.class)
                .block();

        if (response != null && !response.getChoices().isEmpty()) {
            return response.getChoices().getFirst().getMessage().getContent();
        }

        return "Unable to generate briefing at this time.";
    }

    // ── STEP 4: Parse AI response into structured object ──────

    private BriefingResponse parseBriefing(String aiText) {
        String greeting = extractField(aiText, "GREETING:");
        String summary = extractField(aiText, "SUMMARY:");
        String focus = extractField(aiText, "FOCUS:");

        List<String> insights = new ArrayList<>();
        String insight1 = extractField(aiText, "INSIGHT_1:");
        String insight2 = extractField(aiText, "INSIGHT_2:");
        String insight3 = extractField(aiText, "INSIGHT_3:");

        if (!insight1.isEmpty()) insights.add(insight1);
        if (!insight2.isEmpty()) insights.add(insight2);
        if (!insight3.isEmpty()) insights.add(insight3);

        return BriefingResponse.builder()
                .date(LocalDate.now())
                .greeting(greeting)
                .overallSummary(summary)
                .insights(insights)
                .focusForToday(focus)
                .build();
    }

    // Extracts value after a label from AI text
    // e.g. "GREETING: Good morning!" → "Good morning!"
    private String extractField(String text, String label) {
        try {
            int start = text.indexOf(label);
            if (start == -1) return "";
            start += label.length();
            int end = text.indexOf("\n", start);
            if (end == -1) end = text.length();
            return text.substring(start, end).trim();
        } catch (Exception e) {
            return "";
        }
    }
}