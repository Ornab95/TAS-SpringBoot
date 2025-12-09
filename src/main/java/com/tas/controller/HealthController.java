package com.tas.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "*")
public class HealthController {
    
    @GetMapping
    public String health() {
        return "Backend is running!";
    }
}