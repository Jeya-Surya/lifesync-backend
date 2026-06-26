package com.lifesync.goal.repository;

import com.lifesync.goal.model.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, Long> {

    Optional<Milestone> findByIdAndGoalUserEmail(Long id, String userEmail);
}
