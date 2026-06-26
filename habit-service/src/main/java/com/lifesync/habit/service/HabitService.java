package com.lifesync.habit.service;

import com.lifesync.habit.dto.HabitRequest;
import com.lifesync.habit.dto.HabitResponse;
import com.lifesync.habit.dto.StreakResponse;
import com.lifesync.habit.model.Habit;
import com.lifesync.habit.model.HabitFrequency;
import com.lifesync.habit.model.HabitLog;
import com.lifesync.habit.repository.HabitLogRepository;
import com.lifesync.habit.repository.HabitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitRepository habitRepository;
    private final HabitLogRepository habitLogRepository;

    public HabitResponse createHabit(String userEmail, HabitRequest request) {
        HabitFrequency frequency = HabitFrequency.DAILY;

        if(request.getFrequency() != null) {
            frequency = HabitFrequency.valueOf(request.getFrequency().toUpperCase());
        }

        Habit habit = Habit.builder()
                .userEmail(userEmail)
                .name(request.getName())
                .description(request.getDescription())
                .frequency(frequency)
                .build();

        return mapToResponse(habitRepository.save(habit));
    }

    public List<HabitResponse> getAllHabits(String userEmail) {
        return habitRepository.findByUserEmailOrderByCreatedAtDesc(userEmail)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public HabitResponse getHabitById(String userEmail, Long habitId) {
        Habit habit = habitRepository.findByIdAndUserEmail(habitId, userEmail)
                .orElseThrow(() -> new RuntimeException("Habit not found"));

        return mapToResponse(habit);
    }

    public void deleteHabit(String userEmail, Long habitId) {
        Habit habit = habitRepository.findByIdAndUserEmail(habitId, userEmail)
                .orElseThrow(() -> new RuntimeException("Habit not found"));
        habitRepository.delete(habit);
    }

    @Transactional
    public HabitResponse checkIn(String userEmail, Long habitId) {
        Habit habit = habitRepository.findByIdAndUserEmail(habitId, userEmail)
                .orElseThrow(() -> new RuntimeException("Habit not found"));

        LocalDate toady = LocalDate.now();

        Optional<HabitLog> existingLog = habitLogRepository.findByHabitIdAndCheckDate(habitId, toady);

        if(existingLog.isPresent()) {
            throw new RuntimeException("Already checked in today for this habit");
        }

        HabitLog log = HabitLog.builder()
                .habit(habit)
                .checkDate(toady)
                .build();
        habitLogRepository.save(log);

        LocalDate yesterday = toady.minusDays(1);
        Optional<HabitLog> yesterdayLog = habitLogRepository.findByHabitIdAndCheckDate(habitId, yesterday);

        if(yesterdayLog.isPresent()) {
            habit.setCurrentStreak(habit.getCurrentStreak() + 1);
        } else {
            habit.setCurrentStreak(1);
        }

        if (habit.getCurrentStreak() > habit.getLongestStreak()) {
            habit.setLongestStreak(habit.getCurrentStreak());
        }

        return mapToResponse(habitRepository.save(habit));
    }

    public StreakResponse getStreak(String userEmail, Long habitId) {
        Habit habit = habitRepository.findByIdAndUserEmail(habitId, userEmail)
                .orElseThrow(() -> new RuntimeException("Habit not found"));

        boolean checkedInToday = habitLogRepository
                .findByHabitIdAndCheckDate(habitId, LocalDate.now())
                .isPresent();

        return StreakResponse.builder()
                .habitId(habit.getId())
                .habitName(habit.getName())
                .currentStreak(habit.getCurrentStreak())
                .longestStreak(habit.getLongestStreak())
                .checkedInToday(checkedInToday)
                .build();
    }

    public List<HabitLog> getLogs(String userEmail, Long habitId) {
        habitRepository.findByIdAndUserEmail(habitId, userEmail)
                .orElseThrow(() -> new RuntimeException("Habit not found"));
        return habitLogRepository.findByHabitIdOrderByCheckDateDesc(habitId);
    }

    private HabitResponse mapToResponse(Habit habit) {
        boolean checkedInToday = habitLogRepository
                .findByHabitIdAndCheckDate(habit.getId(), LocalDate.now())
                .isPresent();

        return HabitResponse.builder()
                .id(habit.getId())
                .name(habit.getName())
                .description(habit.getDescription())
                .frequency(habit.getFrequency())
                .currentStreak(habit.getCurrentStreak())
                .longestStreak(habit.getLongestStreak())
                .checkedInToday(checkedInToday)
                .createdAt(habit.getCreatedAt())
                .build();

    }
}
