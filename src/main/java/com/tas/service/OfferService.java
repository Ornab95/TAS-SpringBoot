package com.tas.service;

import com.tas.dto.OfferRequest;
import com.tas.entity.Application;
import com.tas.entity.Offer;
import com.tas.enums.OfferStatus;
import com.tas.repository.ApplicationRepository;
import com.tas.repository.OfferRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OfferService {
    
    private static final Logger logger = LoggerFactory.getLogger(OfferService.class);
    
    @Autowired
    private OfferRepository offerRepository;
    
    @Autowired
    private ApplicationRepository applicationRepository;

    public Offer createOffer(OfferRequest request) {
        logger.info("Creating offer for application ID: {}", request.getApplicationId());
        
        Optional<Application> application = applicationRepository.findById(request.getApplicationId());
        if (application.isEmpty()) {
            throw new RuntimeException("Application not found");
        }
        
        Offer offer = new Offer(application.get(), request.getSalary(), request.getPositionTitle());
        offer.setStartDate(request.getStartDate());
        offer.setOfferExpiryDate(request.getOfferExpiryDate());
        offer.setBenefits(request.getBenefits());
        offer.setTerms(request.getTerms());
        
        return offerRepository.save(offer);
    }

    public List<Offer> getOffersByApplication(Long applicationId) {
        return offerRepository.findByApplicationId(applicationId);
    }

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public Offer updateOfferStatus(Long offerId, OfferStatus status) {
        Optional<Offer> offer = offerRepository.findById(offerId);
        
        if (offer.isEmpty()) {
            throw new RuntimeException("Offer not found");
        }
        
        Offer existingOffer = offer.get();
        existingOffer.setStatus(status);
        
        if (status == OfferStatus.ACCEPTED || status == OfferStatus.REJECTED) {
            existingOffer.setRespondedAt(LocalDateTime.now());
        }
        
        return offerRepository.save(existingOffer);
    }

    public Offer getOfferById(Long offerId) {
        return offerRepository.findById(offerId)
            .orElseThrow(() -> new RuntimeException("Offer not found"));
    }

    public void deleteOffer(Long offerId) {
        offerRepository.deleteById(offerId);
    }
}