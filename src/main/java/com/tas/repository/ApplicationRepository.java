package com.tas.repository;

import com.tas.entity.Application;
import com.tas.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByCandidateId(Long candidateId);
    List<Application> findByJobId(Long jobId);
    List<Application> findByStatus(ApplicationStatus status);
    List<Application> findByAssignedRecruiterId(Long recruiterId);
    boolean existsByJobIdAndCandidateId(Long jobId, Long candidateId);
    
    @Query("SELECT a FROM Application a JOIN FETCH a.job j JOIN FETCH j.postedBy JOIN FETCH a.candidate WHERE a.job.id = :jobId")
    List<Application> findByJobIdWithJobDetails(Long jobId);
    
    @Query("SELECT COUNT(a) FROM Application a WHERE a.job.id = :jobId")
    Long countByJobId(Long jobId);
    
    @Query("SELECT a.sourceChannel, COUNT(a) FROM Application a GROUP BY a.sourceChannel")
    List<Object[]> getApplicationsBySource();
    
    Long countByStatus(ApplicationStatus status);
    
    @Query("SELECT COUNT(DISTINCT a.candidate.id) FROM Application a")
    Long countDistinctCandidates();
}