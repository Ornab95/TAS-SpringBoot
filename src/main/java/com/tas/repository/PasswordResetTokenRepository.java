package com.tas.repository;

import com.tas.entity.PasswordResetToken;
import com.tas.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByTokenAndUsedFalseAndExpiryDateAfter(String token, LocalDateTime now);
    void deleteByUser(User user);
    void deleteByExpiryDateBefore(LocalDateTime now);
}