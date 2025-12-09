package com.tas.service;

import com.tas.dto.InterviewRequest;
import com.tas.entity.Application;
import com.tas.entity.Interview;
import com.tas.entity.User;
import com.tas.enums.InterviewStatus;
import com.tas.event.InterviewScheduledEvent;
import com.tas.repository.ApplicationRepository;
import com.tas.repository.InterviewRepository;
import com.tas.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InterviewService {
    
    private static final Logger logger = LoggerFactory.getLogger(InterviewService.class);
    
    @Autowired
    private InterviewRepository interviewRepository;
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    


    public Interview scheduleInterview(InterviewRequest request) {
        logger.info("Scheduling interview for application ID: {}", request.getApplicationId());
        
        Optional<Application> application = applicationRepository.findById(request.getApplicationId());
        Optional<User> interviewer = userRepository.findById(request.getInterviewerId());
        
        if (application.isEmpty()) {
            throw new RuntimeException("Application not found");
        }
        
        if (interviewer.isEmpty()) {
            throw new RuntimeException("Interviewer not found");
        }
        
        Interview interview = new Interview(
            application.get(),
            interviewer.get(),
            request.getScheduledAt(),
            request.getInterviewType()
        );
        
        if (request.getMeetingLink() != null) {
            interview.setMeetingLink(request.getMeetingLink());
        }
        
        Interview savedInterview = interviewRepository.save(interview);
        
        // Publish event for email notification
        eventPublisher.publishEvent(new InterviewScheduledEvent(this, savedInterview));
        

        
        return savedInterview;
    }

    public List<Interview> getInterviewsByApplication(Long applicationId) {
        return interviewRepository.findByApplicationId(applicationId);
    }

    public List<Interview> getInterviewsByInterviewer(Long interviewerId) {
        return interviewRepository.findByInterviewerId(interviewerId);
    }

    public Interview updateInterviewFeedback(Long interviewId, String feedback) {
        Optional<Interview> interview = interviewRepository.findById(interviewId);
        
        if (interview.isEmpty()) {
            throw new RuntimeException("Interview not found");
        }
        
        Interview existingInterview = interview.get();
        existingInterview.setFeedback(feedback);
        existingInterview.setStatus(InterviewStatus.COMPLETED);
        
        Interview updatedInterview = interviewRepository.save(existingInterview);
        
        return updatedInterview;
    }

    public Interview updateInterviewStatus(Long interviewId, InterviewStatus status) {
        Optional<Interview> interview = interviewRepository.findById(interviewId);
        
        if (interview.isEmpty()) {
            throw new RuntimeException("Interview not found");
        }
        
        Interview existingInterview = interview.get();
        existingInterview.setStatus(status);
        
        Interview updatedInterview = interviewRepository.save(existingInterview);
        
        return updatedInterview;
    }

    public List<Interview> getUpcomingInterviews(Long interviewerId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfWeek = now.plusDays(7);
        
        return interviewRepository.findByInterviewerAndDateRange(interviewerId, now, endOfWeek);
    }
}