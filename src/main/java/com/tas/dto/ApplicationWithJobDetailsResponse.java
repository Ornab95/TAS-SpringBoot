package com.tas.dto;

import com.tas.entity.Application;
import com.tas.enums.ApplicationStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApplicationWithJobDetailsResponse {
    private Long id;
    private String coverLetter;
    private String resumeFileName;
    private String portfolioUrl;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
    private CandidateInfo candidate;
    private JobInfo job;
    
    @Data
    public static class CandidateInfo {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
    }
    
    @Data
    public static class JobInfo {
        private Long id;
        private String title;
        private String department;
        private String location;
        private PostedByInfo postedBy;
    }
    
    @Data
    public static class PostedByInfo {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
    }
    
    public static ApplicationWithJobDetailsResponse fromApplication(Application application) {
        ApplicationWithJobDetailsResponse response = new ApplicationWithJobDetailsResponse();
        response.setId(application.getId());
        response.setCoverLetter(application.getCoverLetter());
        response.setResumeFileName(application.getResumeFileName());
        response.setPortfolioUrl(application.getPortfolioUrl());
        response.setStatus(application.getStatus());
        response.setAppliedAt(application.getAppliedAt());
        
        // Candidate info
        CandidateInfo candidateInfo = new CandidateInfo();
        candidateInfo.setId(application.getCandidate().getId());
        candidateInfo.setFirstName(application.getCandidate().getFirstName());
        candidateInfo.setLastName(application.getCandidate().getLastName());
        candidateInfo.setEmail(application.getCandidate().getEmail());
        response.setCandidate(candidateInfo);
        
        // Job info
        JobInfo jobInfo = new JobInfo();
        jobInfo.setId(application.getJob().getId());
        jobInfo.setTitle(application.getJob().getTitle());
        jobInfo.setDepartment(application.getJob().getDepartment());
        jobInfo.setLocation(application.getJob().getLocation());
        
        // Posted by info
        if (application.getJob().getPostedBy() != null) {
            PostedByInfo postedByInfo = new PostedByInfo();
            postedByInfo.setId(application.getJob().getPostedBy().getId());
            postedByInfo.setFirstName(application.getJob().getPostedBy().getFirstName());
            postedByInfo.setLastName(application.getJob().getPostedBy().getLastName());
            postedByInfo.setEmail(application.getJob().getPostedBy().getEmail());
            jobInfo.setPostedBy(postedByInfo);
        }
        
        response.setJob(jobInfo);
        return response;
    }
}