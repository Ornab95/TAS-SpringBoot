package com.tas.repository;

import com.tas.entity.Interview;
import com.tas.enums.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    
    List<Interview> findByApplicationId(Long applicationId);
    
    List<Interview> findByInterviewerId(Long interviewerId);
    
    List<Interview> findByStatus(InterviewStatus status);
    
    @Query("SELECT i FROM Interview i WHERE i.interviewer.id = :interviewerId AND i.scheduledAt BETWEEN :start AND :end")
    List<Interview> findByInterviewerAndDateRange(@Param("interviewerId") Long interviewerId, 
                                                 @Param("start") LocalDateTime start, 
                                                 @Param("end") LocalDateTime end);
}