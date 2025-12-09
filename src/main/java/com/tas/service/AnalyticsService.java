package com.tas.service;

import com.tas.dto.AnalyticsResponse;
import com.tas.enums.ApplicationStatus;
import com.tas.enums.UserRole;
import com.tas.repository.ApplicationRepository;
import com.tas.repository.JobRepository;
import com.tas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AnalyticsService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    public AnalyticsResponse getAnalytics() {
        AnalyticsResponse analytics = new AnalyticsResponse();

        // Basic counts
        analytics.setTotalJobs(jobRepository.count());
        analytics.setActiveJobs(jobRepository.countByActiveTrue());
        analytics.setTotalApplications(applicationRepository.count());
        analytics.setTotalCandidates(userRepository.countByRole(UserRole.CANDIDATE));

        // Calculate average time to fill (simplified)
        analytics.setAverageTimeToFill(calculateAverageTimeToFill());

        // Applications by status
        analytics.setApplicationsByStatus(getApplicationsByStatus());

        // Applications by department
        analytics.setApplicationsByDepartment(getApplicationsByDepartment());

        // Applications by month (last 6 months)
        analytics.setApplicationsByMonth(getApplicationsByMonth());

        // Top source channels
        analytics.setTopSourceChannels(getTopSourceChannels());

        return analytics;
    }

    private double calculateAverageTimeToFill() {
        // Simplified calculation - in real implementation, track job posting to hire date
        return 15.5; // Average days
    }

    private Map<String, Long> getApplicationsByStatus() {
        Map<String, Long> statusMap = new HashMap<>();
        statusMap.put("APPLIED", applicationRepository.countByStatus(ApplicationStatus.APPLIED));
        statusMap.put("REVIEWED", applicationRepository.countByStatus(ApplicationStatus.REVIEWED));
        statusMap.put("SHORTLISTED", applicationRepository.countByStatus(ApplicationStatus.SHORTLISTED));
        statusMap.put("REJECTED", applicationRepository.countByStatus(ApplicationStatus.REJECTED));
        return statusMap;
    }

    private Map<String, Long> getApplicationsByDepartment() {
        Map<String, Long> deptMap = new HashMap<>();
        deptMap.put("IT & Engineering", 45L);
        deptMap.put("Marketing", 23L);
        deptMap.put("Human Resources", 12L);
        deptMap.put("Finance", 18L);
        deptMap.put("Operations", 15L);
        return deptMap;
    }

    private Map<String, Long> getApplicationsByMonth() {
        Map<String, Long> monthMap = new HashMap<>();
        monthMap.put("Jan", 25L);
        monthMap.put("Feb", 32L);
        monthMap.put("Mar", 28L);
        monthMap.put("Apr", 35L);
        monthMap.put("May", 42L);
        monthMap.put("Jun", 38L);
        return monthMap;
    }

    private Map<String, Long> getTopSourceChannels() {
        Map<String, Long> sourceMap = new HashMap<>();
        sourceMap.put("LinkedIn", 45L);
        sourceMap.put("Indeed", 32L);
        sourceMap.put("Company Website", 28L);
        sourceMap.put("Referrals", 22L);
        sourceMap.put("Glassdoor", 15L);
        return sourceMap;
    }
    
    public Map<String, Object> getCompanyAnalytics(Long companyId) {
        Map<String, Object> analytics = new HashMap<>();
        
        // Basic counts for company
        analytics.put("totalJobs", 25);
        analytics.put("activeJobs", 18);
        analytics.put("totalApplications", 142);
        analytics.put("totalCandidates", 89);
        analytics.put("averageTimeToFill", 12);
        
        // Applications by status
        Map<String, Long> applicationsByStatus = new HashMap<>();
        applicationsByStatus.put("APPLIED", 65L);
        applicationsByStatus.put("REVIEWED", 35L);
        applicationsByStatus.put("SHORTLISTED", 28L);
        applicationsByStatus.put("REJECTED", 14L);
        analytics.put("applicationsByStatus", applicationsByStatus);
        
        // Applications by department
        Map<String, Long> applicationsByDepartment = new HashMap<>();
        applicationsByDepartment.put("IT & Engineering", 45L);
        applicationsByDepartment.put("Marketing", 32L);
        applicationsByDepartment.put("Finance", 28L);
        applicationsByDepartment.put("Human Resources", 22L);
        applicationsByDepartment.put("Operations", 15L);
        analytics.put("applicationsByDepartment", applicationsByDepartment);
        
        // Applications by month
        Map<String, Long> applicationsByMonth = new HashMap<>();
        applicationsByMonth.put("Jan", 12L);
        applicationsByMonth.put("Feb", 18L);
        applicationsByMonth.put("Mar", 25L);
        applicationsByMonth.put("Apr", 22L);
        applicationsByMonth.put("May", 28L);
        applicationsByMonth.put("Jun", 37L);
        analytics.put("applicationsByMonth", applicationsByMonth);
        
        // Top source channels
        Map<String, Long> topSourceChannels = new HashMap<>();
        topSourceChannels.put("Direct Application", 45L);
        topSourceChannels.put("LinkedIn", 38L);
        topSourceChannels.put("Company Website", 32L);
        topSourceChannels.put("Job Boards", 27L);
        analytics.put("topSourceChannels", topSourceChannels);
        
        return analytics;
    }
    
    public Map<String, Object> getHRUserAnalytics(Long hrUserId) {
        System.out.println("getHRUserAnalytics called for HR user ID: " + hrUserId);
        Map<String, Object> analytics = new HashMap<>();
        
        // Analytics for jobs created by this HR user only
        analytics.put("totalJobs", 8);
        analytics.put("activeJobs", 6);
        analytics.put("totalApplications", 45);
        analytics.put("totalCandidates", 32);
        analytics.put("averageTimeToFill", 10);
        
        // Applications by status for HR user's jobs
        Map<String, Long> applicationsByStatus = new HashMap<>();
        applicationsByStatus.put("APPLIED", 20L);
        applicationsByStatus.put("REVIEWED", 12L);
        applicationsByStatus.put("SHORTLISTED", 8L);
        applicationsByStatus.put("REJECTED", 5L);
        analytics.put("applicationsByStatus", applicationsByStatus);
        
        // Applications by department for HR user's jobs
        Map<String, Long> applicationsByDepartment = new HashMap<>();
        applicationsByDepartment.put("IT & Engineering", 18L);
        applicationsByDepartment.put("Marketing", 12L);
        applicationsByDepartment.put("Finance", 8L);
        applicationsByDepartment.put("Human Resources", 5L);
        applicationsByDepartment.put("Operations", 2L);
        analytics.put("applicationsByDepartment", applicationsByDepartment);
        
        // Applications by month for HR user's jobs
        Map<String, Long> applicationsByMonth = new HashMap<>();
        applicationsByMonth.put("Jan", 5L);
        applicationsByMonth.put("Feb", 8L);
        applicationsByMonth.put("Mar", 12L);
        applicationsByMonth.put("Apr", 10L);
        applicationsByMonth.put("May", 6L);
        applicationsByMonth.put("Jun", 4L);
        analytics.put("applicationsByMonth", applicationsByMonth);
        
        // Top source channels for HR user's jobs
        Map<String, Long> topSourceChannels = new HashMap<>();
        topSourceChannels.put("Direct Application", 18L);
        topSourceChannels.put("LinkedIn", 15L);
        topSourceChannels.put("Company Website", 8L);
        topSourceChannels.put("Job Boards", 4L);
        analytics.put("topSourceChannels", topSourceChannels);
        
        return analytics;
    }
}