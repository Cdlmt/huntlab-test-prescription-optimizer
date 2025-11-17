package com.cosium.prescription.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;
    
    @Column(nullable = false)
    private Instant createdAt;
    
    @Column(nullable = false)
    private String status;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
    
    @Column
    private String discountCode;
    
    public Order() {}
    
    public Order(Patient patient, Prescription prescription, Instant createdAt, String status) {
        this.patient = patient;
        this.prescription = prescription;
        this.createdAt = createdAt;
        this.status = status;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    
    public Prescription getPrescription() { return prescription; }
    public void setPrescription(Prescription prescription) { this.prescription = prescription; }
    
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
    
    public String getDiscountCode() { return discountCode; }
    public void setDiscountCode(String discountCode) { this.discountCode = discountCode; }
}
