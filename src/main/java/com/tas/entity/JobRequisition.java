package com.tas.entity;

import com.tas.enums.RequisitionStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "job_requisitions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobRequisition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(columnDefinition = "TEXT")
    private String requirements;
    
    private String department;
    private String location;
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private Integer positions = 1;
    
    @Enumerated(EnumType.STRING)
    private RequisitionStatus status = RequisitionStatus.DRAFT;
    
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;
    
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime approvedAt;
    private LocalDateTime deadline;
}