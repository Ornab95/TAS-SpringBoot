package com.tas.dto;

import com.tas.enums.ApplicationStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApplicationResponse {
    private Long id;
    private Long jobId;
    private String jobTitle;
    private String candidateName;
    private String candidateEmail;
    private String coverLetter;
    private String resumeFileName;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
}