package com.lifesync.jobtracker.repository;

import com.lifesync.jobtracker.model.InterviewRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRoundRepository
        extends JpaRepository<InterviewRound, Long> {

    List<InterviewRound> findByApplicationIdOrderByRoundDateAsc(Long applicationId);

    Optional<InterviewRound> findByIdAndApplicationUserEmail(
            Long id, String userEmail);
}