package com.cosium.prescription.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "prescriptions")
public class Prescription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @Column(nullable = false)
    private Instant issuedAt;
    
    @Column(nullable = false)
    private Instant expiresAt;
    
    @Column(nullable = false)
    private String doctorName;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(nullable = false)
    private Double leftEyeSphere;
    
    @Column(nullable = false)
    private Double rightEyeSphere;
    
    public Prescription() {}
    
    public Prescription(Patient patient, Instant issuedAt, Instant expiresAt, 
                       String doctorName, Double leftEyeSphere, Double rightEyeSphere) {
        this.patient = patient;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.doctorName = doctorName;
        this.leftEyeSphere = leftEyeSphere;
        this.rightEyeSphere = rightEyeSphere;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    
    public Instant getIssuedAt() { return issuedAt; }
    public void setIssuedAt(Instant issuedAt) { this.issuedAt = issuedAt; }
    
    public Instant getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
    
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public Double getLeftEyeSphere() { return leftEyeSphere; }
    public void setLeftEyeSphere(Double leftEyeSphere) { this.leftEyeSphere = leftEyeSphere; }
    
    public Double getRightEyeSphere() { return rightEyeSphere; }
    public void setRightEyeSphere(Double rightEyeSphere) { this.rightEyeSphere = rightEyeSphere; }
}
