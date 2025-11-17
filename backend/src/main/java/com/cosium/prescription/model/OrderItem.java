package com.cosium.prescription.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @Column(nullable = false)
    private String lensType;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private Double unitPrice;
    
    public OrderItem() {}
    
    public OrderItem(Order order, String lensType, Integer quantity, Double unitPrice) {
        this.order = order;
        this.lensType = lensType;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    
    public String getLensType() { return lensType; }
    public void setLensType(String lensType) { this.lensType = lensType; }
    
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    
    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }
}
