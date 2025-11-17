package com.cosium.prescription.controller;

import com.cosium.prescription.dto.OrderDTO;
import com.cosium.prescription.service.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    
    private final OrderService orderService;
    
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    /**
     * Get orders by period
     * 
     * PERFORMANCE ISSUE (identified in profiling):
     * - N+1 query problem with patient and orderItems
     * - In-memory filtering instead of SQL WHERE clause
     * - Average response time: 850ms for 100 orders
     * - Expected: < 100ms
     * 
     * Sample profiling output:
     * - Query 1: SELECT * FROM orders (1 query)
     * - Query 2-101: SELECT * FROM patients WHERE id = ? (100 queries)
     * - Query 102-201: SELECT * FROM order_items WHERE order_id = ? (100 queries)
     * Total: 201 queries
     */
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrdersByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        
        List<OrderDTO> orders = orderService.findOrdersByPeriod(from, to);
        return ResponseEntity.ok(orders);
    }
}

