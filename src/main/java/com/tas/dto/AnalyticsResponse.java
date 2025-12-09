package com.tas.dto;

import java.util.Map;

public class AnalyticsResponse {
    private long totalJobs;
    private long activeJobs;
    private long totalApplications;
    private long totalCandidates;
    private double averageTimeToFill;
    private Map<String, Long> applicationsByStatus;
    private Map<String, Long> applicationsByDepartment;
    private Map<String, Long> applicationsByMonth;
    private Map<String, Long> topSourceChannels;

    // Constructors
    public AnalyticsResponse() {}

    // Getters and Setters
    public long getTotalJobs() { return totalJobs; }
    public void setTotalJobs(long totalJobs) { this.totalJobs = totalJobs; }

    public long getActiveJobs() { return activeJobs; }
    public void setActiveJobs(long activeJobs) { this.activeJobs = activeJobs; }

    public long getTotalApplications() { return totalApplications; }
    public void setTotalApplications(long totalApplications) { this.totalApplications = totalApplications; }

    public long getTotalCandidates() { return totalCandidates; }
    public void setTotalCandidates(long totalCandidates) { this.totalCandidates = totalCandidates; }

    public double getAverageTimeToFill() { return averageTimeToFill; }
    public void setAverageTimeToFill(double averageTimeToFill) { this.averageTimeToFill = averageTimeToFill; }

    public Map<String, Long> getApplicationsByStatus() { return applicationsByStatus; }
    public void setApplicationsByStatus(Map<String, Long> applicationsByStatus) { this.applicationsByStatus = applicationsByStatus; }

    public Map<String, Long> getApplicationsByDepartment() { return applicationsByDepartment; }
    public void setApplicationsByDepartment(Map<String, Long> applicationsByDepartment) { this.applicationsByDepartment = applicationsByDepartment; }

    public Map<String, Long> getApplicationsByMonth() { return applicationsByMonth; }
    public void setApplicationsByMonth(Map<String, Long> applicationsByMonth) { this.applicationsByMonth = applicationsByMonth; }

    public Map<String, Long> getTopSourceChannels() { return topSourceChannels; }
    public void setTopSourceChannels(Map<String, Long> topSourceChannels) { this.topSourceChannels = topSourceChannels; }
}