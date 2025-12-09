package com.tas.dto;

import com.tas.enums.InterviewType;
import java.time.LocalDateTime;

public class InterviewRequest {
    private Long applicationId;
    private Long interviewerId;
    private LocalDateTime scheduledAt;
    private InterviewType interviewType;
    private String meetingLink;

    // Constructors
    public InterviewRequest() {}

    public InterviewRequest(Long applicationId, Long interviewerId, LocalDateTime scheduledAt, InterviewType interviewType) {
        this.applicationId = applicationId;
        this.interviewerId = interviewerId;
        this.scheduledAt = scheduledAt;
        this.interviewType = interviewType;
    }

    // Getters and Setters
    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }

    public Long getInterviewerId() { return interviewerId; }
    public void setInterviewerId(Long interviewerId) { this.interviewerId = interviewerId; }

    public LocalDateTime getScheduledAt() { return scheduledAt; }
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }

    public InterviewType getInterviewType() { return interviewType; }
    public void setInterviewType(InterviewType interviewType) { this.interviewType = interviewType; }

    public String getMeetingLink() { return meetingLink; }
    public void setMeetingLink(String meetingLink) { this.meetingLink = meetingLink; }
}