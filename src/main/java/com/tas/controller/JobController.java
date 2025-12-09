package com.tas.controller;

import com.tas.entity.Job;
import com.tas.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "*")
public class JobController {
    
    private static final Logger logger = LoggerFactory.getLogger(JobController.class);
    
    @Autowired
    private JobService jobService;
    
    @GetMapping("/public")
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllActiveJobs());
    }
    
    @GetMapping
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'RECRUITER', 'HIRING_MANAGER', 'SYSTEM_ADMIN')")
    public ResponseEntity<List<Job>> getMyJobs() {
        return ResponseEntity.ok(jobService.getJobsByCurrentUser());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(jobService.getJobById(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'RECRUITER', 'HIRING_MANAGER', 'SYSTEM_ADMIN')")
    public ResponseEntity<?> createJob(@Valid @RequestBody Job job) {
        try {
            Job createdJob = jobService.createJob(job);
            logger.info("Job created successfully with ID: {}", createdJob.getId());
            return ResponseEntity.ok(Map.of("message", "Job created successfully", "jobId", createdJob.getId()));
        } catch (Exception e) {
            logger.error("Error creating job", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to create job"));
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'RECRUITER', 'HIRING_MANAGER', 'SYSTEM_ADMIN')")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @Valid @RequestBody Job job) {
        try {
            logger.info("Updating job with ID: {}", id);
            jobService.updateJob(id, job);
            logger.info("Job updated successfully with ID: {}", id);
            return ResponseEntity.ok(Map.of("message", "Job updated successfully"));
        } catch (Exception e) {
            logger.error("Error updating job with ID: {}", id, e);
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to update job"));
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'RECRUITER', 'HIRING_MANAGER', 'SYSTEM_ADMIN')")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        try {
            jobService.deleteJob(id);
            logger.info("Job deleted successfully with ID: {}", id);
            return ResponseEntity.ok(Map.of("message", "Job deleted successfully"));
        } catch (Exception e) {
            logger.error("Error deleting job with ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }
}

