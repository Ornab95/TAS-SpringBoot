package com.tas.controller;

import com.tas.entity.Company;
import com.tas.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/company")
@CrossOrigin(origins = "http://localhost:4200")
public class CompanyController {
    
    @Autowired
    private CompanyService companyService;
    
    @PostMapping("/register")
    public ResponseEntity<?> registerCompany(@RequestBody Map<String, Object> request) {
        try {
            // Extract company data
            Company company = new Company();
            company.setName((String) request.get("companyName"));
            company.setEmail((String) request.get("companyEmail"));
            company.setDescription((String) request.get("description"));
            company.setWebsite((String) request.get("website"));
            company.setIndustry((String) request.get("industry"));
            company.setLocation((String) request.get("location"));
            
            // Extract admin user data
            String adminEmail = (String) request.get("adminEmail");
            String adminPassword = (String) request.get("adminPassword");
            String adminFirstName = (String) request.get("adminFirstName");
            String adminLastName = (String) request.get("adminLastName");
            
            Company registeredCompany = companyService.registerCompany(
                company, adminEmail, adminPassword, adminFirstName, adminLastName
            );
            
            return ResponseEntity.ok(Map.of(
                "message", "Company registered successfully",
                "companyId", registeredCompany.getId()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}