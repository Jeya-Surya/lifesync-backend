package com.lifesync.habit.repository;

import com.lifesync.habit.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {

    List<Habit> findByUserEmailOrderByCreatedAtDesc(String userEmail);

    Optional<Habit> findByIdAndUserEmail(Long id, String userEmail);
}
