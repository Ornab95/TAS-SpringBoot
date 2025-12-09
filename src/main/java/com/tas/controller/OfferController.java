package com.tas.controller;

import com.tas.dto.OfferRequest;
import com.tas.entity.Offer;
import com.tas.enums.OfferStatus;
import com.tas.service.OfferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@CrossOrigin(origins = "http://localhost:4200")
public class OfferController {
    
    private static final Logger logger = LoggerFactory.getLogger(OfferController.class);
    
    @Autowired
    private OfferService offerService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('HR_ADMIN') or hasRole('RECRUITER') or hasRole('HIRING_MANAGER') or hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<Offer> createOffer(@RequestBody OfferRequest request) {
        try {
            logger.info("Creating offer request received");
            Offer offer = offerService.createOffer(request);
            return ResponseEntity.ok(offer);
        } catch (Exception e) {
            logger.error("Error creating offer: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/application/{applicationId}")
    @PreAuthorize("hasRole('HR_ADMIN') or hasRole('RECRUITER') or hasRole('HIRING_MANAGER') or hasRole('CANDIDATE')")
    public ResponseEntity<List<Offer>> getOffersByApplication(@PathVariable Long applicationId) {
        List<Offer> offers = offerService.getOffersByApplication(applicationId);
        return ResponseEntity.ok(offers);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('HR_ADMIN') or hasRole('RECRUITER') or hasRole('HIRING_MANAGER') or hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<List<Offer>> getAllOffers() {
        List<Offer> offers = offerService.getAllOffers();
        return ResponseEntity.ok(offers);
    }

    @PutMapping("/{offerId}/status")
    @PreAuthorize("hasRole('HR_ADMIN') or hasRole('RECRUITER') or hasRole('HIRING_MANAGER') or hasRole('CANDIDATE')")
    public ResponseEntity<Offer> updateOfferStatus(@PathVariable Long offerId, @RequestBody OfferStatus status) {
        try {
            Offer offer = offerService.updateOfferStatus(offerId, status);
            return ResponseEntity.ok(offer);
        } catch (Exception e) {
            logger.error("Error updating offer status: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{offerId}")
    @PreAuthorize("hasRole('HR_ADMIN') or hasRole('RECRUITER') or hasRole('HIRING_MANAGER') or hasRole('CANDIDATE')")
    public ResponseEntity<Offer> getOffer(@PathVariable Long offerId) {
        try {
            Offer offer = offerService.getOfferById(offerId);
            return ResponseEntity.ok(offer);
        } catch (Exception e) {
            logger.error("Error fetching offer: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{offerId}")
    @PreAuthorize("hasRole('HR_ADMIN') or hasRole('RECRUITER') or hasRole('HIRING_MANAGER') or hasRole('SYSTEM_ADMIN')")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long offerId) {
        try {
            offerService.deleteOffer(offerId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting offer: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}