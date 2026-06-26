package com.lifesync.goal.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "milestones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Milestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tile;

    @Builder.Default
    private Boolean isCompleted = false;

    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    @ToString.Exclude
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Goal goal;
}
