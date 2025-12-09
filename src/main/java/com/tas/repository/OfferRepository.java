package com.tas.repository;

import com.tas.entity.Offer;
import com.tas.enums.OfferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {
    
    List<Offer> findByApplicationId(Long applicationId);
    
    List<Offer> findByStatus(OfferStatus status);
    
    Optional<Offer> findByApplicationIdAndStatus(Long applicationId, OfferStatus status);
    
    long countByStatus(OfferStatus status);
}