package com.tas.controller;

import com.tas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
    
    @GetMapping("/count")
    public ResponseEntity<?> getUserCount() {
        return ResponseEntity.ok("User count: " + userRepository.count());
    }
    
    @GetMapping("/auth-test")
    public ResponseEntity<?> testAuth() {
        return ResponseEntity.ok("Authentication test endpoint - public access");
    }
    
    @GetMapping("/protected-test")
    public ResponseEntity<?> testProtected() {
        return ResponseEntity.ok("This endpoint requires authentication");
    }
}