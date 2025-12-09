package com.tas.entity;

import com.tas.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    
    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;
    
    @Column(columnDefinition = "TEXT")
    private String coverLetter;
    
    private String resumeFileName;
    private String resumeFilePath;
    
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] resumeData;
    private String portfolioUrl;
    
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.APPLIED;
    
    private Integer screeningScore;
    private String sourceChannel; // LinkedIn, Indeed, Referral, etc.
    
    @ManyToOne
    @JoinColumn(name = "assigned_recruiter")
    private User assignedRecruiter;
    
    private LocalDateTime appliedAt = LocalDateTime.now();
    private LocalDateTime lastUpdated = LocalDateTime.now();
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Transient
    private String companyName;
    
    public String getCompanyName() {
        return job != null ? job.getCompanyName() : null;
    }
}