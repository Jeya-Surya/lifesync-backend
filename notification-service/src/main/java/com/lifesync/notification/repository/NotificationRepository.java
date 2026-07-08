package com.lifesync.notification.repository;

import com.lifesync.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserEmailOrderByCreatedAtDesc(String userEmail);

    List<Notification> findByUserEmailAndIsReadFalse(String userEmail);

    long countByUserEmailAndIsReadFalse(String userEmail);
}
