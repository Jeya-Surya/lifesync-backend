package com.lifesync.habit.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "habit_logs", uniqueConstraints = @UniqueConstraint(
            columnNames = {"habit_id", "check_date"}
        ))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habit_id", nullable = false)
    @ToString.Exclude
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Habit habit;

    @Column(name = "check_date", nullable = false)
    private LocalDate checkDate;

    @Enumerated
    @Builder.Default
    private CheckInStatus status = CheckInStatus.DONE;
}
