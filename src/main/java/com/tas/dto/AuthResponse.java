package com.tas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private String token;
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String department;
    private String profilePicture;
}