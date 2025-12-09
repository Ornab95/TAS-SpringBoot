package com.tas.controller;

import com.tas.dto.InterviewRequest;
import com.tas.entity.Interview;
import com.tas.enums.InterviewStatus;
import com.tas.service.InterviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@CrossOrigin(origins = "http://localhost:4200")
public class InterviewController {
    
    private static final Logger logger = LoggerFactory.getLogger(InterviewController.class);
    
    @Autowired
    private InterviewService interviewService;

    @PostMapping("/schedule")
    @PreAuthorize("hasRole('HR_ADMIN') or hasRole('RECRUITER') or hasRole('HIRING_MANAGER') or hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<Interview> scheduleInterview(@RequestBody InterviewRequest request) {
        try {
            logger.info("Scheduling interview request received");
            Interview interview = interviewService.scheduleInterview(request);
            return ResponseEntity.ok(interview);
        } catch (Exception e) {
            logger.error("Error scheduling interview: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/application/{applicationId}")
    @PreAuthorize("hasRole('HR_ADMIN') or hasRole('RECRUITER') or hasRole('HIRING_MANAGER') or hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<List<Interview>> getInterviewsByApplication(@PathVariable Long applicationId) {
        List<Interview> interviews = interviewService.getInterviewsByApplication(applicationId);
        return ResponseEntity.ok(interviews);
    }

    @GetMapping("/interviewer/{interviewerId}")
    @PreAuthorize("hasRole('HR_ADMIN') or hasRole('RECRUITER') or hasRole('HIRING_MANAGER') or hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<List<Interview>> getInterviewsByInterviewer(@PathVariable Long interviewerId) {
        List<Interview> interviews = interviewService.getInterviewsByInterviewer(interviewerId);
        return ResponseEntity.ok(interviews);
    }

    @PutMapping("/{interviewId}/feedback")
    @PreAuthorize("hasRole('HR_ADMIN') or hasRole('RECRUITER') or hasRole('HIRING_MANAGER') or hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<Interview> updateFeedback(@PathVariable Long interviewId, @RequestBody String feedback) {
        try {
            Interview interview = interviewService.updateInterviewFeedback(interviewId, feedback);
            return ResponseEntity.ok(interview);
        } catch (Exception e) {
            logger.error("Error updating interview feedback: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{interviewId}/status")
    @PreAuthorize("hasRole('HR_ADMIN') or hasRole('RECRUITER') or hasRole('HIRING_MANAGER') or hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<Interview> updateStatus(@PathVariable Long interviewId, @RequestParam String status) {
        try {
            InterviewStatus interviewStatus = InterviewStatus.valueOf(status);
            Interview interview = interviewService.updateInterviewStatus(interviewId, interviewStatus);
            return ResponseEntity.ok(interview);
        } catch (Exception e) {
            logger.error("Error updating interview status: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/upcoming/{interviewerId}")
    @PreAuthorize("hasRole('HR_ADMIN') or hasRole('RECRUITER') or hasRole('HIRING_MANAGER') or hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<List<Interview>> getUpcomingInterviews(@PathVariable Long interviewerId) {
        List<Interview> interviews = interviewService.getUpcomingInterviews(interviewerId);
        return ResponseEntity.ok(interviews);
    }
}