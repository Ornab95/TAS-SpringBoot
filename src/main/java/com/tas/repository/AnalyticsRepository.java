package com.tas.repository;

import com.tas.entity.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {
    List<Analytics> findByEventType(String eventType);
    List<Analytics> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT COUNT(a) FROM Analytics a WHERE a.eventType = ?1 AND a.timestamp >= ?2")
    Long countByEventTypeAndTimestampAfter(String eventType, LocalDateTime timestamp);
}