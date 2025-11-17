package com.cosium.prescription.dto;

public class OrderItemDTO {
    
    private String lensType;
    private Integer quantity;
    private Double unitPrice;
    
    public OrderItemDTO() {}
    
    public OrderItemDTO(String lensType, Integer quantity, Double unitPrice) {
        this.lensType = lensType;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
    
    // Getters and Setters
    public String getLensType() { return lensType; }
    public void setLensType(String lensType) { this.lensType = lensType; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }
}
