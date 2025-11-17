package com.cosium.prescription.service;

import com.cosium.prescription.dto.EligibilityResponse;
import com.cosium.prescription.model.Prescription;
import com.cosium.prescription.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class EligibilityService {
    
    private final PrescriptionRepository prescriptionRepository;
    
    public EligibilityService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }
    
    public EligibilityResponse checkEligibility(Long prescriptionId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
            .orElseThrow(() -> new RuntimeException("Prescription not found"));
        
        // BUG: Naive timezone conversion - uses system default timezone instead of Europe/Paris
        // BUG: Incorrect date comparison - converts to LocalDate and loses time precision
        LocalDate now = LocalDate.now();
        LocalDate issuedDate = prescription.getIssuedAt()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
        LocalDate expiryDate = prescription.getExpiresAt()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
        
        // BUG: Uses isBefore/isAfter instead of inclusive bounds (should use !isBefore && !isAfter)
        // This causes edge case issues for prescriptions issued late at night
        boolean dateValid = now.isAfter(issuedDate) && now.isBefore(expiryDate);
        
        boolean patientActive = prescription.getPatient().getActive();
        
        boolean covered = dateValid && patientActive;
        String reason = buildReason(dateValid, patientActive);
        
        return new EligibilityResponse(prescriptionId, covered, reason);
    }
    
    private String buildReason(boolean dateValid, boolean patientActive) {
        if (!patientActive) {
            return "Patient is not active";
        }
        if (!dateValid) {
            return "Prescription is expired or not yet valid";
        }
        return "Prescription is valid and covered";
    }
}

