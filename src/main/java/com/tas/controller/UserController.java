package com.tas.controller;

import com.tas.entity.User;
import com.tas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody Map<String, String> updates, Authentication authentication) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            
            if (updates.containsKey("firstName")) {
                user.setFirstName(updates.get("firstName"));
            }
            if (updates.containsKey("lastName")) {
                user.setLastName(updates.get("lastName"));
            }
            // Email updates disabled for security reasons
            if (updates.containsKey("phone")) {
                user.setPhone(updates.get("phone"));
            }
            if (updates.containsKey("department")) {
                user.setDepartment(updates.get("department"));
            }
            
            userRepository.save(user);
            
            String successMessage = "Profile updated successfully " + user.getFirstName() + " " + user.getLastName();
            return ResponseEntity.ok(Map.of("message", successMessage));
        } catch (Exception e) {
            logger.error("Error updating user", e);
            return ResponseEntity.status(500).body("Error updating user");
        }
    }
    
    @PutMapping("/{userId}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long userId, @RequestBody Map<String, String> passwordData, Authentication authentication) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            
            String currentPassword = passwordData.get("currentPassword");
            String newPassword = passwordData.get("newPassword");
            
            if (currentPassword == null || newPassword == null) {
                return ResponseEntity.badRequest().body("Current password and new password are required");
            }
            
            // Verify current password
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                return ResponseEntity.badRequest().body("Current password is incorrect");
            }
            
            // Update password
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            
            String successMessage = "Password changed successfully " + user.getFirstName() + " " + user.getLastName();
            return ResponseEntity.ok(Map.of("message", successMessage));
        } catch (Exception e) {
            logger.error("Error changing password", e);
            return ResponseEntity.status(500).body("Error changing password");
        }
    }
    
    @PostMapping("/{userId}/profile-picture")
    public ResponseEntity<?> uploadProfilePicture(@PathVariable Long userId, @RequestParam("profilePicture") MultipartFile file, Authentication authentication) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Please select a file"));
            }
            
            // Validate file type
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Only image files are allowed"));
            }
            
            String fileName = UUID.randomUUID().toString() + ".png";
            String uploadDir = "uploads/profile-pictures/";
            
            // Create directory if it doesn't exist
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            Path filePath = Paths.get(uploadDir + fileName);
            Files.write(filePath, file.getBytes());
            
            user.setProfilePictureFileName(fileName);
            user.setProfilePictureFilePath(uploadDir + fileName);
            userRepository.save(user);
            
            String successMessage = "Profile picture uploaded successfully " + user.getFirstName() + " " + user.getLastName();
            return ResponseEntity.ok(Map.of(
                "message", successMessage,
                "profilePictureUrl", "/api/files/profile-pictures/" + fileName
            ));
        } catch (Exception e) {
            logger.error("Error uploading profile picture", e);
            return ResponseEntity.status(500).body(Map.of("error", "Error uploading profile picture"));
        }
    }
}