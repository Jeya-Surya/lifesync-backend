package com.lifesync.jobtracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "job_applications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String role;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    private LocalDate appliedDate;

    private LocalDate followUpDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    private String jobUrl;

    private LocalDate createdAt;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true, fetch =  FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<InterviewRound> rounds = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
        if(this.appliedDate == null) {
            this.appliedDate = LocalDate.now();
        }
    }
}
