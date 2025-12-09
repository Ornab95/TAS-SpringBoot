package com.tas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tas.service.EmailService;
import java.util.Map;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "http://localhost:4200")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody Map<String, String> emailData) {
        try {
            String to = emailData.get("to");
            String subject = emailData.get("subject");
            String body = emailData.get("body");
            
            emailService.sendEmail(to, subject, body);
            return ResponseEntity.ok("Email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }
    }

    @PostMapping("/shortlist")
    public ResponseEntity<String> sendShortlistEmail(@RequestBody Map<String, String> emailData) {
        try {
            String candidateEmail = emailData.get("candidateEmail");
            String candidateName = emailData.get("candidateName");
            String jobTitle = emailData.get("jobTitle");
            String companyName = emailData.get("companyName");
            
            emailService.sendShortlistEmail(candidateEmail, candidateName, jobTitle, companyName);
            return ResponseEntity.ok("Shortlist email sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send shortlist email: " + e.getMessage());
        }
    }
}