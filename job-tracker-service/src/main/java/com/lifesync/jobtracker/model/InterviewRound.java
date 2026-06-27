package com.lifesync.jobtracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "interview_rounds")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterviewRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roundName;

    private LocalDate roundDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RoundOutcome outcome = RoundOutcome.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    @ToString.Exclude
    @com.fasterxml.jackson.annotation.JsonIgnore
    private JobApplication application;
}
