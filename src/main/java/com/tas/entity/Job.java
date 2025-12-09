package com.tas.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "requisition_id")
    private JobRequisition requisition;
    
    @Column(nullable = false)
    @jakarta.validation.constraints.NotBlank(message = "Title is required")
    @jakarta.validation.constraints.Size(max = 200, message = "Title must be less than 200 characters")
    private String title;
    
    @Column(columnDefinition = "TEXT")
    @jakarta.validation.constraints.NotBlank(message = "Description is required")
    @jakarta.validation.constraints.Size(max = 5000, message = "Description must be less than 5000 characters")
    private String description;
    
    @Column(columnDefinition = "TEXT")
    @jakarta.validation.constraints.NotBlank(message = "Requirements are required")
    @jakarta.validation.constraints.Size(max = 5000, message = "Requirements must be less than 5000 characters")
    private String requirements;
    
    private String department;
    private String location;
    private String companyName;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    
    private boolean active = true;
    private boolean published = false;
    
    @ManyToOne
    @JoinColumn(name = "posted_by")
    private User postedBy;
    
    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonIgnore
    private Company company;
    
    private LocalDateTime postedAt = LocalDateTime.now();
    private LocalDate deadline;
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @Transient
    private Long applicationCount = 0L;
}