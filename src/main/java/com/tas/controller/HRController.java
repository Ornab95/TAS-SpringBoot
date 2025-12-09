package com.tas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.tas.service.AnalyticsService;
import com.tas.entity.User;
import com.tas.repository.UserRepository;
import java.util.Map;

@RestController
@RequestMapping("/api/hr")
@CrossOrigin(origins = "*")
public class HRController {
    
    @Autowired
    private AnalyticsService analyticsService;
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/analytics")
    @PreAuthorize("hasAnyRole('HR_ADMIN', 'RECRUITER', 'HIRING_MANAGER')")
    public ResponseEntity<Map<String, Object>> getAnalytics(Authentication authentication) {
        try {
            if (authentication == null) {
                return ResponseEntity.status(401).body(Map.of("error", "No authentication found"));
            }
            
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            System.out.println("HR Analytics - User ID: " + user.getId() + ", Email: " + email);
            Map<String, Object> analytics = analyticsService.getHRUserAnalytics(user.getId());
            System.out.println("HR Analytics - Returning data for totalJobs: " + analytics.get("totalJobs"));
            return ResponseEntity.ok(analytics);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to fetch analytics: " + e.getMessage()));
        }
    }
}