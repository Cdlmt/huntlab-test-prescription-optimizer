package com.cosium.prescription.dto;

public class EligibilityResponse {
    
    private Long prescriptionId;
    private boolean covered;
    private String reason;
    
    public EligibilityResponse() {}
    
    public EligibilityResponse(Long prescriptionId, boolean covered, String reason) {
        this.prescriptionId = prescriptionId;
        this.covered = covered;
        this.reason = reason;
    }
    
    // Getters and Setters
    public Long getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(Long prescriptionId) { this.prescriptionId = prescriptionId; }
    
    public boolean isCovered() { return covered; }
    public void setCovered(boolean covered) { this.covered = covered; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
