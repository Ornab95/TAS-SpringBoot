package com.tas.service;

import com.tas.entity.Interview;
import com.tas.entity.Notification;
import com.tas.entity.User;
import com.tas.event.InterviewScheduledEvent;
import com.tas.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.format.DateTimeFormatter;

@Service
public class NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private TemplateEngine templateEngine;
    
    @Autowired
    private NotificationRepository notificationRepository;

    @EventListener
    public void handleInterviewScheduled(InterviewScheduledEvent event) {
        Interview interview = event.getInterview();
        logger.info("Sending interview notification for interview ID: {}", interview.getId());
        
        try {
            sendInterviewNotification(interview);
            createInterviewNotifications(interview);
        } catch (Exception e) {
            logger.error("Failed to send interview notification: {}", e.getMessage());
        }
    }
    
    private void createInterviewNotifications(Interview interview) {
        Notification candidateNotif = new Notification();
        candidateNotif.setUser(interview.getApplication().getCandidate());
        candidateNotif.setTitle("Interview Scheduled");
        candidateNotif.setMessage("Your interview for " + interview.getApplication().getJob().getTitle() + " has been scheduled");
        candidateNotif.setType("INTERVIEW");
        candidateNotif.setRelatedEntityType("INTERVIEW");
        candidateNotif.setRelatedEntityId(interview.getId());
        notificationRepository.save(candidateNotif);
        
        Notification interviewerNotif = new Notification();
        interviewerNotif.setUser(interview.getInterviewer());
        interviewerNotif.setTitle("Interview Assignment");
        interviewerNotif.setMessage("You have been assigned to interview " + 
            interview.getApplication().getCandidate().getFirstName() + " " + 
            interview.getApplication().getCandidate().getLastName());
        interviewerNotif.setType("INTERVIEW");
        interviewerNotif.setRelatedEntityType("INTERVIEW");
        interviewerNotif.setRelatedEntityId(interview.getId());
        notificationRepository.save(interviewerNotif);
    }
    
    public void createNotification(User user, String title, String message, String type, String entityType, Long entityId) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRelatedEntityType(entityType);
        notification.setRelatedEntityId(entityId);
        notificationRepository.save(notification);
    }

    private void sendInterviewNotification(Interview interview) throws MessagingException {
        // Send to candidate
        String candidateEmail = interview.getApplication().getCandidate().getEmail();
        sendEmail(candidateEmail, "Interview Scheduled", "interview-scheduled", interview);
        
        // Send to interviewer
        String interviewerEmail = interview.getInterviewer().getEmail();
        sendEmail(interviewerEmail, "Interview Assignment", "interview-assigned", interview);
    }

    private void sendEmail(String to, String subject, String template, Interview interview) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        Context context = new Context();
        context.setVariable("candidateName", 
            interview.getApplication().getCandidate().getFirstName() + " " + 
            interview.getApplication().getCandidate().getLastName());
        context.setVariable("interviewerName", 
            interview.getInterviewer().getFirstName() + " " + 
            interview.getInterviewer().getLastName());
        context.setVariable("jobTitle", interview.getApplication().getJob().getTitle());
        context.setVariable("scheduledTime", 
            interview.getScheduledAt().format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a")));
        context.setVariable("interviewType", interview.getInterviewType().toString());
        context.setVariable("meetingLink", interview.getMeetingLink());
        
        String htmlContent = templateEngine.process(template, context);
        
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom("noreply@tas.com");
        
        mailSender.send(message);
        logger.info("Email sent successfully to: {}", to);
    }
}