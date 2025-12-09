package com.tas.service;

import com.tas.entity.Company;
import com.tas.entity.User;
import com.tas.enums.UserRole;
import com.tas.repository.CompanyRepository;
import com.tas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class CompanyService {
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Transactional
    public Company registerCompany(Company company, String adminEmail, String adminPassword, String adminFirstName, String adminLastName) {
        // Check if company email already exists
        if (companyRepository.existsByEmail(company.getEmail())) {
            throw new RuntimeException("Company with this email already exists");
        }
        
        // Check if admin email already exists
        if (userRepository.existsByEmail(adminEmail)) {
            throw new RuntimeException("User with this email already exists");
        }
        
        // Save company
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());
        Company savedCompany = companyRepository.save(company);
        
        // Create admin user for the company
        User adminUser = new User();
        adminUser.setEmail(adminEmail);
        adminUser.setPassword(passwordEncoder.encode(adminPassword));
        adminUser.setFirstName(adminFirstName);
        adminUser.setLastName(adminLastName);
        adminUser.setRole(UserRole.HR_ADMIN);
        adminUser.setCompany(savedCompany);
        adminUser.setActive(true);
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setUpdatedAt(LocalDateTime.now());
        
        userRepository.save(adminUser);
        
        return savedCompany;
    }
}