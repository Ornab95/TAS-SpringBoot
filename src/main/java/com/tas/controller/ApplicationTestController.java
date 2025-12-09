package com.tas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class ApplicationTestController {
    
    @GetMapping("/application")
    public ResponseEntity<String> testApplication() {
        return ResponseEntity.ok("Application feature is working!");
    }
}