package com.tas.entity;

import com.tas.enums.OfferStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private Application application;

    @Column(name = "salary", nullable = false)
    private BigDecimal salary;

    @Column(name = "position_title", nullable = false)
    private String positionTitle;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "offer_expiry_date")
    private LocalDate offerExpiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OfferStatus status = OfferStatus.PENDING;

    @Column(name = "benefits", columnDefinition = "TEXT")
    private String benefits;

    @Column(name = "terms", columnDefinition = "TEXT")
    private String terms;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    // Constructors
    public Offer() {}

    public Offer(Application application, BigDecimal salary, String positionTitle) {
        this.application = application;
        this.salary = salary;
        this.positionTitle = positionTitle;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Application getApplication() { return application; }
    public void setApplication(Application application) { this.application = application; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }

    public String getPositionTitle() { return positionTitle; }
    public void setPositionTitle(String positionTitle) { this.positionTitle = positionTitle; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getOfferExpiryDate() { return offerExpiryDate; }
    public void setOfferExpiryDate(LocalDate offerExpiryDate) { this.offerExpiryDate = offerExpiryDate; }

    public OfferStatus getStatus() { return status; }
    public void setStatus(OfferStatus status) { this.status = status; }

    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }

    public String getTerms() { return terms; }
    public void setTerms(String terms) { this.terms = terms; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getRespondedAt() { return respondedAt; }
    public void setRespondedAt(LocalDateTime respondedAt) { this.respondedAt = respondedAt; }
}