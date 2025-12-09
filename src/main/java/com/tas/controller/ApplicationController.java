package com.tas.controller;

import com.tas.entity.Application;
import com.tas.entity.Job;
import com.tas.entity.User;
import com.tas.dto.ApplicationWithJobDetailsResponse;
import com.tas.repository.ApplicationRepository;
import com.tas.repository.JobRepository;
import com.tas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
// import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:4200")
public class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications(Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(401).build();
            }
            List<Application> applications = applicationRepository.findAll();
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createApplication(
            @RequestParam("jobId") Long jobId,
            @RequestParam("coverLetter") String coverLetter,
            @RequestParam(value = "portfolioUrl", required = false) String portfolioUrl,
            @RequestParam(value = "resume", required = false) MultipartFile resume,
            Authentication authentication) {
        try {
            // Validate input
            if (coverLetter == null || coverLetter.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (coverLetter.trim().length() < 50) {
                return ResponseEntity.badRequest().build();
            }
            
            String userEmail = authentication.getName();
            User candidate = userRepository.findByEmail(userEmail).orElse(null);
            if (candidate == null) {
                return ResponseEntity.status(401).build();
            }
            
            Job job = jobRepository.findById(jobId).orElse(null);
            if (job == null) {
                return ResponseEntity.status(404).build();
            }
            
            // Check if candidate already applied for this job
            boolean alreadyApplied = applicationRepository.existsByJobIdAndCandidateId(jobId, candidate.getId());
            if (alreadyApplied) {
                return ResponseEntity.badRequest().body(Map.of("error", "You have already applied for this job"));
            }
            
            Application application = new Application();
            application.setJob(job);
            application.setCandidate(candidate);
            application.setCoverLetter(coverLetter);
            application.setPortfolioUrl(portfolioUrl);
            
            if (resume != null && !resume.isEmpty()) {
                String fileName = UUID.randomUUID().toString() + "_" + resume.getOriginalFilename();
                String uploadDir = "uploads/resumes/";
                
                // Create directory if it doesn't exist
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                
                Path filePath = Paths.get(uploadDir + fileName);
                Files.write(filePath, resume.getBytes());
                
                application.setResumeFileName(resume.getOriginalFilename());
                application.setResumeFilePath(fileName);
                application.setResumeData(resume.getBytes());
            }
            
            applicationRepository.save(application);
            String successMessage = "Application submitted successfully " + candidate.getFirstName() + " " + candidate.getLastName();
            return ResponseEntity.ok(Map.of("message", successMessage));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationWithJobDetailsResponse>> getApplicationsByJob(@PathVariable Long jobId, Authentication authentication) {
        try {
            List<Application> applications = applicationRepository.findByJobIdWithJobDetails(jobId);
            List<ApplicationWithJobDetailsResponse> response = applications.stream()
                .map(ApplicationWithJobDetailsResponse::fromApplication)
                .toList();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/my-applications")
    public ResponseEntity<List<Application>> getMyApplications(Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            User candidate = userRepository.findByEmail(userEmail).orElse(null);
            if (candidate == null) {
                return ResponseEntity.status(401).build();
            }
            
            List<Application> applications = applicationRepository.findByCandidateId(candidate.getId());
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<?> updateApplicationStatus(@PathVariable Long applicationId, @RequestParam String status, Authentication authentication) {
        try {
            Application application = applicationRepository.findById(applicationId).orElse(null);
            if (application == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Application not found"));
            }
            
            application.setStatus(com.tas.enums.ApplicationStatus.valueOf(status));
            applicationRepository.save(application);
            
            String successMessage = "Application status updated successfully for " + application.getCandidate().getFirstName() + " " + application.getCandidate().getLastName();
            return ResponseEntity.ok(Map.of("message", successMessage));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error updating application status"));
        }
    }
}