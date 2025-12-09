package com.tas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("ornabbiswass@gmail.com");
            
            mailSender.send(message);
        } catch (Exception e) {

            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    public void sendShortlistEmail(String candidateEmail, String candidateName, String jobTitle, String companyName) {
        String subject = "Congratulations! You've been shortlisted for " + jobTitle;
        String body = String.format(
            "Dear %s,\n\n" +
            "Congratulations! We are pleased to inform you that you have been shortlisted for the position of %s at %s.\n\n" +
            "We will contact you soon with further details about the next steps in the selection process.\n\n" +
            "Best regards,\n%s Team",
            candidateName, jobTitle, companyName, companyName
        );
        
        sendEmail(candidateEmail, subject, body);
    }

    public void sendPasswordResetCode(String email, String firstName, String code) {
        String subject = "Password Reset Verification Code - TAS";
        String body = String.format(
            "Dear %s,\n\n" +
            "You have requested to reset your password for your TAS account.\n\n" +
            "Your verification code is: %s\n\n" +
            "This code will expire in 15 minutes.\n\n" +
            "If you did not request this password reset, please ignore this email.\n\n" +
            "Best regards,\nTAS Team",
            firstName, code
        );
        
        sendEmail(email, subject, body);
    }
}