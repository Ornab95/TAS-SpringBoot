package com.tas.controller;

import com.tas.entity.User;
import com.tas.entity.Application;
import com.tas.entity.SiteSetting;
import com.tas.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('SYSTEM_ADMIN')")
public class AdminController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    
    @Autowired
    private AdminService adminService;
    
    // User Management
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        users.forEach(user -> user.setPassword(null)); // Don't return passwords
        return ResponseEntity.ok(users);
    }
    
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user, HttpServletRequest request) {
        try {
            User createdUser = adminService.createUser(user);
            createdUser.setPassword(null);
            
            // Log the event securely
            String userPrincipal = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "system";
            logger.info("User created by admin: {} for email: {}", userPrincipal, user.getEmail());
            
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            logger.error("Error creating user", e);
            return ResponseEntity.status(500).body(null);
        }
    }
    
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> updates, HttpServletRequest request) {
        try {
            User updatedUser = adminService.updateUser(id, updates);
            updatedUser.setPassword(null);
            
            // Log the event securely
            String userPrincipal = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "system";
            logger.info("User updated by admin: {} for user ID: {}", userPrincipal, id);
            
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            logger.error("Error updating user with ID: {}", id, e);
            return ResponseEntity.status(500).body(null);
        }
    }
    
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, HttpServletRequest request) {
        try {
            adminService.deleteUser(id);
            
            // Log the event securely
            String userPrincipal = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "system";
            logger.info("User deleted by admin: {} for user ID: {}", userPrincipal, id);
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting user with ID: {}", id, e);
            return ResponseEntity.status(500).build();
        }
    }
    
    // Site Settings
    @GetMapping("/settings")
    public ResponseEntity<List<SiteSetting>> getAllSettings() {
        return ResponseEntity.ok(adminService.getAllSettings());
    }
    
    @PostMapping("/settings")
    public ResponseEntity<SiteSetting> createSetting(@RequestBody SiteSetting setting, HttpServletRequest request) {
        try {
            SiteSetting createdSetting = adminService.updateSetting(setting.getSettingKey(), setting.getSettingValue());
            
            // Log the event securely
            String userPrincipal = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "system";
            logger.info("Setting created by admin: {} for key: {}", userPrincipal, setting.getSettingKey());
            
            return ResponseEntity.ok(createdSetting);
        } catch (Exception e) {
            logger.error("Error creating setting", e);
            return ResponseEntity.status(500).body(null);
        }
    }
    
    @DeleteMapping("/settings/{key}")
    public ResponseEntity<Void> deleteSetting(@PathVariable String key, HttpServletRequest request) {
        try {
            adminService.deleteSetting(key);
            
            // Log the event securely
            String userPrincipal = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "system";
            logger.info("Setting deleted by admin: {} for key: {}", userPrincipal, key);
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting setting with key: {}", key, e);
            return ResponseEntity.status(500).build();
        }
    }
    
    // Analytics & Dashboard
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }
    
    // Permissions (Role-based access is handled by @PreAuthorize)
    @GetMapping("/permissions/check")
    public ResponseEntity<Map<String, Boolean>> checkPermissions() {
        Map<String, Boolean> permissions = Map.of(
            "canManageUsers", true,
            "canManageSettings", true,
            "canViewAnalytics", true,
            "canManageJobs", true
        );
        return ResponseEntity.ok(permissions);
    }
    
    @GetMapping("/users/{userId}/applications")
    public ResponseEntity<List<Application>> getUserApplications(@PathVariable Long userId) {
        try {
            List<Application> applications = adminService.getUserApplications(userId);
            return ResponseEntity.ok(applications);
        } catch (Exception e) {
            logger.error("Error getting user applications for user ID: {}", userId, e);
            return ResponseEntity.status(500).body(null);
        }
    }
    

}