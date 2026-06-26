package com.lifesync.habit.repository;

import com.lifesync.habit.model.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {

    Optional<HabitLog> findByHabitIdAndCheckDate(Long habitId, LocalDate date);

    List<HabitLog> findByHabitIdOrderByCheckDateDesc(Long habitId);
}
