package com.tas.config;

import com.tas.entity.User;
import com.tas.enums.UserRole;
import com.tas.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * Initializes application data on startup.
 * Creates default admin user if none exists.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        try {
            logger.info("=== DataInitializer running ===");
            
            // Check if any SYSTEM_ADMIN exists
            long adminCount = userRepository.findByRole(UserRole.SYSTEM_ADMIN).size();
            logger.info("Found {} SYSTEM_ADMIN users", adminCount);
            
            if (adminCount == 0) {
                logger.info("Creating default admin user...");
                User admin = new User();
                admin.setEmail("admin@tas.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setFirstName("System");
                admin.setLastName("Administrator");
                admin.setRole(UserRole.SYSTEM_ADMIN);
                admin.setDepartment("IT");
                admin.setActive(true);
                admin.setCreatedAt(LocalDateTime.now());
                admin.setUpdatedAt(LocalDateTime.now());
                
                User savedAdmin = userRepository.save(admin);
                logger.info("✓ Created default admin user: {} with ID: {}", savedAdmin.getEmail(), savedAdmin.getId());
            } else {
                logger.info("SYSTEM_ADMIN user already exists, skipping creation");
            }
            
            logger.info("=== DataInitializer completed ===");
        } catch (Exception e) {
            logger.error("!!! Error during data initialization: {}", e.getMessage(), e);
            e.printStackTrace();
        }
    }
}