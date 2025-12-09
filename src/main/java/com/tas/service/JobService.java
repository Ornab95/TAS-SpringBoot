package com.tas.service;

import com.tas.entity.Job;
import com.tas.entity.User;
import com.tas.repository.ApplicationRepository;
import com.tas.repository.JobRepository;
import com.tas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JobService {
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private UserRepository userRepository;
    

    
    public List<Job> getAllActiveJobs() {
        List<Job> jobs = jobRepository.findByActiveTrue();
        jobs.forEach(job -> {
            Long count = applicationRepository.countByJobId(job.getId());
            job.setApplicationCount(count);
        });
        return jobs;
    }

    public List<Job> getJobsByCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName() != null) {
            User currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
            if (currentUser != null) {
                List<Job> jobs;
                // SYSTEM_ADMIN can see all jobs
                if ("SYSTEM_ADMIN".equals(currentUser.getRole().name())) {
                    jobs = jobRepository.findAll();
                } else {
                    jobs = jobRepository.findByPostedBy(currentUser);
                }
                jobs.forEach(job -> {
                    Long count = applicationRepository.countByJobId(job.getId());
                    job.setApplicationCount(count);
                });
                return jobs;
            }
        }
        return List.of();
    }
    
    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
    }
    
    public Job createJob(Job job) {
        // Handle empty deadline
        if (job.getDeadline() == null) {
            job.setDeadline(null);
        }
        
        // Set current user as poster and company
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName() != null) {
            User currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
            if (currentUser != null) {
                job.setPostedBy(currentUser);
                if (currentUser.getCompany() != null) {
                    job.setCompany(currentUser.getCompany());
                }
            }
        }
        
        Job savedJob = jobRepository.save(job);
        return savedJob;
    }
    
    public Job updateJob(Long id, Job jobDetails) {
        Job job = getJobById(id);
        
        // Check if current user is the job poster or SYSTEM_ADMIN
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName() != null) {
            User currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
            if (currentUser != null && !"SYSTEM_ADMIN".equals(currentUser.getRole().name()) && 
                job.getPostedBy() != null && !job.getPostedBy().getId().equals(currentUser.getId())) {
                throw new RuntimeException("You can only update jobs you created");
            }
        }
        
        job.setTitle(jobDetails.getTitle());
        job.setDescription(jobDetails.getDescription());
        job.setRequirements(jobDetails.getRequirements());
        job.setDepartment(jobDetails.getDepartment());
        job.setLocation(jobDetails.getLocation());
        job.setMinSalary(jobDetails.getMinSalary());
        job.setMaxSalary(jobDetails.getMaxSalary());
        job.setDeadline(jobDetails.getDeadline());
        job.setActive(jobDetails.isActive());
        Job updatedJob = jobRepository.save(job);
        return updatedJob;
    }
    
    public void deleteJob(Long id) {
        Job job = getJobById(id);
        
        // Check if current user is the job poster or SYSTEM_ADMIN
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName() != null) {
            User currentUser = userRepository.findByEmail(auth.getName()).orElse(null);
            if (currentUser != null && !"SYSTEM_ADMIN".equals(currentUser.getRole().name()) && 
                job.getPostedBy() != null && !job.getPostedBy().getId().equals(currentUser.getId())) {
                throw new RuntimeException("You can only delete jobs you created");
            }
        }
        
        job.setActive(false);
        jobRepository.save(job);
    }
    
    public Long getTotalJobsCount() {
        return jobRepository.count();
    }
    
    public Long getActiveJobsCount() {
        return jobRepository.countByActiveTrue();
    }
}