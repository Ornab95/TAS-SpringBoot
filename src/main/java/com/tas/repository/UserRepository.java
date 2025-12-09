package com.tas.repository;

import com.tas.entity.User;
import com.tas.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRole(UserRole role);
    List<User> findByDepartment(String department);
    List<User> findByActiveTrue();
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= ?1")
    Long countByCreatedAtAfter(LocalDateTime createdAt);
    
    Long countByRole(UserRole role);
}