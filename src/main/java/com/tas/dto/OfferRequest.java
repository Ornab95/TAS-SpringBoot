package com.tas.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OfferRequest {
    private Long applicationId;
    private BigDecimal salary;
    private String positionTitle;
    private LocalDate startDate;
    private LocalDate offerExpiryDate;
    private String benefits;
    private String terms;

    // Constructors
    public OfferRequest() {}

    // Getters and Setters
    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }

    public BigDecimal getSalary() { return salary; }
    public void setSalary(BigDecimal salary) { this.salary = salary; }

    public String getPositionTitle() { return positionTitle; }
    public void setPositionTitle(String positionTitle) { this.positionTitle = positionTitle; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getOfferExpiryDate() { return offerExpiryDate; }
    public void setOfferExpiryDate(LocalDate offerExpiryDate) { this.offerExpiryDate = offerExpiryDate; }

    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }

    public String getTerms() { return terms; }
    public void setTerms(String terms) { this.terms = terms; }
}