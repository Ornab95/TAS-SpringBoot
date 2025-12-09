package com.tas.controller;

// import com.tas.dto.PasswordResetRequest;
import com.tas.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/password-reset")
@CrossOrigin(origins = "http://localhost:4200")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePasswordReset(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String message = passwordResetService.initiatePasswordReset(email);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {
            String code = request.get("code");
            String newPassword = request.get("newPassword");
            String message = passwordResetService.resetPassword(code, newPassword);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/admin-reset/{userId}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<?> adminResetPassword(@PathVariable Long userId, @RequestBody Map<String, String> request) {
        try {
            String newPassword = request.get("newPassword");
            String message = passwordResetService.adminResetPassword(userId, newPassword);
            return ResponseEntity.ok(Map.of("message", message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}