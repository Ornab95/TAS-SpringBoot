package com.tas.service;

import com.tas.entity.User;
import com.tas.entity.Application;
import com.tas.entity.SiteSetting;
import com.tas.entity.Analytics;
import com.tas.enums.UserRole;
import com.tas.repository.UserRepository;
import com.tas.repository.SiteSettingRepository;
import com.tas.repository.AnalyticsRepository;
import com.tas.repository.JobRepository;
import com.tas.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class AdminService {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SiteSettingRepository siteSettingRepository;
    
    @Autowired
    private AnalyticsRepository analyticsRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // User Management
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public User createUser(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(passwordEncoder.encode("defaultPassword123"));
        }
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    public User updateUser(Long id, Map<String, Object> updates) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Update fields only if they are provided in the map
        if (updates.containsKey("firstName")) {
            existingUser.setFirstName((String) updates.get("firstName"));
        }
        if (updates.containsKey("lastName")) {
            existingUser.setLastName((String) updates.get("lastName"));
        }
        if (updates.containsKey("email")) {
            existingUser.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("role")) {
            existingUser.setRole(UserRole.valueOf((String) updates.get("role")));
        }
        if (updates.containsKey("department")) {
            existingUser.setDepartment((String) updates.get("department"));
        }
        if (updates.containsKey("active")) {
            existingUser.setActive((Boolean) updates.get("active"));
        }
        existingUser.setUpdatedAt(LocalDateTime.now());
        
        // Only update password if provided and not empty
        if (updates.containsKey("password")) {
            String password = (String) updates.get("password");
            if (password != null && !password.trim().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(password));
            }
        }
        
        return userRepository.save(existingUser);
    }
    
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    
    // Site Settings
    public List<SiteSetting> getAllSettings() {
        return siteSettingRepository.findAll();
    }
    
    public SiteSetting updateSetting(String key, String value) {
        SiteSetting setting = siteSettingRepository.findBySettingKey(key)
                .orElse(new SiteSetting());
        setting.setSettingKey(key);
        setting.setSettingValue(value);
        return siteSettingRepository.save(setting);
    }
    
    public void deleteSetting(String key) {
        siteSettingRepository.findBySettingKey(key)
                .ifPresent(setting -> siteSettingRepository.delete(setting));
    }
    
    // Analytics
    public Map<String, Object> getDashboardStats() {
        logger.info("Generating dashboard statistics");
        
        long totalUsers = userRepository.count();
        long totalJobs = jobRepository.count();
        long totalApplications = applicationRepository.count();
        
        LocalDateTime lastMonth = LocalDateTime.now().minusDays(30);
        long newUsersThisMonth = userRepository.countByCreatedAtAfter(lastMonth);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("totalJobs", totalJobs);
        stats.put("totalApplications", totalApplications);
        stats.put("newUsersThisMonth", newUsersThisMonth);
        
        logger.info("Dashboard statistics generated successfully");
        return stats;
    }
    
    public void logEvent(String eventType, String eventData, String userEmail, String ipAddress) {
        Analytics analytics = new Analytics();
        analytics.setEventType(eventType);
        analytics.setEventData(eventData);
        analytics.setUserEmail(userEmail);
        analytics.setIpAddress(ipAddress);
        analyticsRepository.save(analytics);
    }
    
    public List<Application> getUserApplications(Long userId) {
        return applicationRepository.findByCandidateId(userId);
    }
}