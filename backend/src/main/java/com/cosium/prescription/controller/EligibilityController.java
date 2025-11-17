package com.cosium.prescription.controller;

import com.cosium.prescription.dto.EligibilityResponse;
import com.cosium.prescription.service.EligibilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prescriptions")
@CrossOrigin(origins = "*")
public class EligibilityController {
    
    private final EligibilityService eligibilityService;
    
    public EligibilityController(EligibilityService eligibilityService) {
        this.eligibilityService = eligibilityService;
    }
    
    @GetMapping("/{id}/eligibility")
    public ResponseEntity<EligibilityResponse> checkEligibility(@PathVariable Long id) {
        EligibilityResponse response = eligibilityService.checkEligibility(id);
        return ResponseEntity.ok(response);
    }
}

