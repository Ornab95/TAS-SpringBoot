package com.tas.controller;

import com.tas.entity.Notification;
import com.tas.entity.User;
import com.tas.repository.NotificationRepository;
import com.tas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/my")
    public ResponseEntity<List<Notification>> getMyNotifications(Authentication authentication) {
        try {
            User user = userRepository.findByEmail(authentication.getName()).orElse(null);
            if (user == null) {
                return ResponseEntity.status(401).build();
            }
            
            List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id, Authentication authentication) {
        try {
            User user = userRepository.findByEmail(authentication.getName()).orElse(null);
            if (user == null) {
                return ResponseEntity.status(401).build();
            }
            
            Notification notification = notificationRepository.findById(id).orElse(null);
            if (notification == null) {
                return ResponseEntity.status(404).build();
            }
            
            if (!notification.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(403).build();
            }
            
            notification.setRead(true);
            notification.setReadAt(LocalDateTime.now());
            notificationRepository.save(notification);
            
            return ResponseEntity.ok(Map.of("message", "Notification marked as read"));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/read-all")
    @Transactional
    public ResponseEntity<?> markAllAsRead(Authentication authentication) {
        try {
            User user = userRepository.findByEmail(authentication.getName()).orElse(null);
            if (user == null) {
                return ResponseEntity.status(401).build();
            }
            
            notificationRepository.markAllAsReadByUserId(user.getId());
            return ResponseEntity.ok(Map.of("message", "All notifications marked as read"));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(Authentication authentication) {
        try {
            User user = userRepository.findByEmail(authentication.getName()).orElse(null);
            if (user == null) {
                return ResponseEntity.status(401).build();
            }
            
            Long count = notificationRepository.countByUserIdAndIsReadFalse(user.getId());
            return ResponseEntity.ok(Map.of("unreadCount", count));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
