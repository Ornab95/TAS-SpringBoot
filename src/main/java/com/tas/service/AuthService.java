package com.tas.service;

import com.tas.config.JwtUtil;
import com.tas.dto.AuthResponse;
import com.tas.dto.LoginRequest;
import com.tas.entity.User;
import com.tas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public AuthResponse login(LoginRequest request) {
        logger.info("Login attempt initiated");
        
        if (request.getEmail() == null || request.getPassword() == null) {
            logger.warn("Login attempt with missing credentials");
            throw new RuntimeException("Email and password are required");
        }
        
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    logger.warn("Login attempt for non-existent user");
                    return new RuntimeException("Invalid credentials");
                });
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.warn("Login attempt with invalid password for user: {}", user.getEmail());
            throw new RuntimeException("Invalid credentials");
        }
        
        logger.info("Successful login for user: {}", user.getEmail());
        String token = jwtUtil.generateToken(user.getEmail());
        String profilePictureUrl = user.getProfilePictureFileName() != null ? 
            "/api/files/profile-pictures/" + user.getProfilePictureFileName() : null;
        String successMessage = "Successfully logged in " + user.getFirstName() + " " + user.getLastName();
        return new AuthResponse(successMessage, token, user.getId(), user.getEmail(), user.getFirstName(), 
                               user.getLastName(), user.getRole().toString(), user.getDepartment(), profilePictureUrl);
    }
    
    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}