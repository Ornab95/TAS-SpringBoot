package com.tas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.tas.service.JobService;
import com.tas.service.ApplicationService;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AnalyticsController {
    
    @Autowired
    private JobService jobService;
    
    @Autowired
    private ApplicationService applicationService;
    
    @GetMapping("/analytics")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<Map<String, Object>> getAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        
        // Basic counts
        analytics.put("totalJobs", jobService.getTotalJobsCount());
        analytics.put("activeJobs", jobService.getActiveJobsCount());
        analytics.put("totalApplications", applicationService.getTotalApplicationsCount());
        analytics.put("totalCandidates", applicationService.getTotalCandidatesCount());
        analytics.put("averageTimeToFill", 15); // Mock data
        
        // Applications by status
        Map<String, Long> applicationsByStatus = new HashMap<>();
        applicationsByStatus.put("APPLIED", applicationService.getApplicationsCountByStatus("APPLIED"));
        applicationsByStatus.put("REVIEWED", applicationService.getApplicationsCountByStatus("REVIEWED"));
        applicationsByStatus.put("SHORTLISTED", applicationService.getApplicationsCountByStatus("SHORTLISTED"));
        applicationsByStatus.put("REJECTED", applicationService.getApplicationsCountByStatus("REJECTED"));
        analytics.put("applicationsByStatus", applicationsByStatus);
        
        // Mock data for other analytics
        Map<String, Long> applicationsByDepartment = new HashMap<>();
        applicationsByDepartment.put("IT & Engineering", 45L);
        applicationsByDepartment.put("Marketing", 23L);
        applicationsByDepartment.put("Sales", 18L);
        applicationsByDepartment.put("HR", 12L);
        analytics.put("applicationsByDepartment", applicationsByDepartment);
        
        Map<String, Long> applicationsByMonth = new HashMap<>();
        applicationsByMonth.put("Jan", 25L);
        applicationsByMonth.put("Feb", 32L);
        applicationsByMonth.put("Mar", 28L);
        applicationsByMonth.put("Apr", 35L);
        analytics.put("applicationsByMonth", applicationsByMonth);
        
        Map<String, Long> topSourceChannels = new HashMap<>();
        topSourceChannels.put("Direct Apply", 45L);
        topSourceChannels.put("LinkedIn", 32L);
        topSourceChannels.put("Job Boards", 18L);
        topSourceChannels.put("Referrals", 12L);
        analytics.put("topSourceChannels", topSourceChannels);
        
        return ResponseEntity.ok(analytics);
    }
}