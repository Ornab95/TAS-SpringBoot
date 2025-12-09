package com.tas.service;

import com.tas.entity.Application;
import com.tas.entity.Job;
import com.tas.entity.User;
import com.tas.enums.ApplicationStatus;
import com.tas.repository.ApplicationRepository;
import com.tas.repository.JobRepository;
import com.tas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ApplicationService {
    
    private static final Logger logger = LoggerFactory.getLogger(ApplicationService.class);
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private final String uploadDir = System.getProperty("user.dir") + "/uploads/resumes/";
    
    public Application submitApplication(Long jobId, Long candidateId, String coverLetter, MultipartFile resume) throws IOException {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        User candidate = userRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if user already applied for this job
        if (applicationRepository.existsByJobIdAndCandidateId(jobId, candidateId)) {
            throw new RuntimeException("You have already applied for this job");
        }
        
        Application application = new Application();
        application.setJob(job);
        application.setCandidate(candidate);
        application.setCoverLetter(coverLetter);
        
        // Save resume file
        if (resume != null && !resume.isEmpty()) {
            // Save to file system
            String fileName = saveResumeFile(resume);
            application.setResumeFileName(resume.getOriginalFilename());
            application.setResumeFilePath(fileName);
            
            // Also save to database
            application.setResumeData(resume.getBytes());
        }
        
        return applicationRepository.save(application);
    }
    
    private String saveResumeFile(MultipartFile file) throws IOException {
        // Validate file type
        String contentType = file.getContentType();
        if (!"application/pdf".equals(contentType)) {
            throw new RuntimeException("Only PDF files are allowed");
        }
        
        // Validate file size (10MB max)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new RuntimeException("File size must be less than 10MB");
        }
        
        // Validate filename
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.contains("..") || originalFilename.contains("/") || originalFilename.contains("\\")) {
            throw new RuntimeException("Invalid filename");
        }
        
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate secure filename
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String fileName = UUID.randomUUID().toString() + fileExtension;
        Path filePath = uploadPath.resolve(fileName);
        
        // Save file
        Files.copy(file.getInputStream(), filePath);
        
        return fileName;
    }
    
    public List<Application> getApplicationsByJob(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }
    
    public List<Application> getApplicationsByCandidate(Long candidateId) {
        return applicationRepository.findByCandidateId(candidateId);
    }
    

    
    public Application updateApplicationStatus(Long applicationId, String status) {
        logger.info("Updating application ID: {} to status: {}", applicationId, status);
        
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        
        try {
            ApplicationStatus newStatus = ApplicationStatus.valueOf(status.toUpperCase());
            application.setStatus(newStatus);
            Application saved = applicationRepository.save(application);
            logger.info("Application status updated successfully for ID: {}", applicationId);
            return saved;
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid status provided: {} for application ID: {}", status, applicationId);
            throw new RuntimeException("Invalid status provided");
        }
    }
    
    public Long getTotalApplicationsCount() {
        return applicationRepository.count();
    }
    
    public Long getTotalCandidatesCount() {
        return applicationRepository.countDistinctCandidates();
    }
    
    public Long getApplicationsCountByStatus(String status) {
        try {
            ApplicationStatus appStatus = ApplicationStatus.valueOf(status.toUpperCase());
            return applicationRepository.countByStatus(appStatus);
        } catch (IllegalArgumentException e) {
            return 0L;
        }
    }
}