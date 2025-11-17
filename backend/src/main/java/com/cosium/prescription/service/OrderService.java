package com.cosium.prescription.service;

import com.cosium.prescription.dto.OrderDTO;
import com.cosium.prescription.dto.OrderItemDTO;
import com.cosium.prescription.model.Order;
import com.cosium.prescription.model.OrderItem;
import com.cosium.prescription.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
    public List<OrderDTO> findOrdersByPeriod(Instant from, Instant to) {
        // BUG: N+1 query problem - findAll() doesn't use JOIN FETCH
        // This will trigger separate queries for patient and orderItems for each order
        List<Order> allOrders = orderRepository.findAll();
        
        // BUG: Filtering in memory instead of in SQL
        // This is inefficient and loads all orders from database
        List<Order> filteredOrders = allOrders.stream()
            .filter(order -> !order.getCreatedAt().isBefore(from) 
                         && !order.getCreatedAt().isAfter(to))
            .toList();
        
        // BUG: Accessing lazy-loaded collections causes additional queries
        return filteredOrders.stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    private OrderDTO toDTO(Order order) {
        // Accessing patient triggers a query if not fetched
        String patientName = order.getPatient().getFirstName() + " " 
                           + order.getPatient().getLastName();
        
        // Accessing orderItems triggers another query if not fetched
        List<OrderItemDTO> items = order.getOrderItems().stream()
            .map(item -> new OrderItemDTO(
                item.getLensType(),
                item.getQuantity(),
                item.getUnitPrice()
            ))
            .collect(Collectors.toList());
        
        double total = order.getOrderItems().stream()
            .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
            .sum();
        
        return new OrderDTO(
            order.getId(),
            patientName,
            order.getCreatedAt(),
            order.getStatus(),
            items,
            total
        );
    }
}

