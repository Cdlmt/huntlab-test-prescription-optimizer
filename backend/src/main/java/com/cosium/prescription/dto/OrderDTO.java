package com.cosium.prescription.dto;

import java.time.Instant;
import java.util.List;

public class OrderDTO {
    
    private Long id;
    private String patientName;
    private Instant createdAt;
    private String status;
    private List<OrderItemDTO> items;
    private Double totalAmount;
    
    public OrderDTO() {}
    
    public OrderDTO(Long id, String patientName, Instant createdAt, String status, 
                    List<OrderItemDTO> items, Double totalAmount) {
        this.id = id;
        this.patientName = patientName;
        this.createdAt = createdAt;
        this.status = status;
        this.items = items;
        this.totalAmount = totalAmount;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
    
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public List<OrderItemDTO> getItems() { return items; }
    public void setItems(List<OrderItemDTO> items) { this.items = items; }
    
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
}
