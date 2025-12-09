package com.tas.service;

import com.tas.entity.PasswordResetToken;
import com.tas.entity.User;
import com.tas.repository.PasswordResetTokenRepository;
import com.tas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
// import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public String initiatePasswordReset(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        
        // Delete existing tokens
        tokenRepository.deleteByUser(user);

        // Create new 6-digit verification code
        String code = String.format("%06d", (int)(Math.random() * 1000000));
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(code);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(resetToken);

        // Send email with code
        emailService.sendPasswordResetCode(user.getEmail(), user.getFirstName(), code);
        
        return "Verification code sent to your email";
    }

    @Transactional
    public String resetPassword(String code, String newPassword) {
        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByTokenAndUsedFalseAndExpiryDateAfter(code, LocalDateTime.now());
        if (tokenOpt.isEmpty()) {
            throw new RuntimeException("Invalid or expired verification code");
        }

        PasswordResetToken resetToken = tokenOpt.get();
        User user = resetToken.getUser();
        
        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        // Mark token as used
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        return "Password reset successfully";
    }

    @Transactional
    public String adminResetPassword(Long userId, String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "Password reset by admin successfully";
    }
}