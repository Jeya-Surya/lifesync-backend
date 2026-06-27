package com.lifesync.jobtracker.repository;

import com.lifesync.jobtracker.model.ApplicationStatus;
import com.lifesync.jobtracker.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByUserEmailOrderByCreatedAtDesc(String userEmail);

    Optional<JobApplication> findByIdAndUserEmail(Long id, String userEmail);

    long countByUserEmailAndStatus(String userEmail, ApplicationStatus status);

    long countByUserEmail(String userEmail);
}
