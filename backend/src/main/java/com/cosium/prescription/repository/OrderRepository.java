package com.cosium.prescription.repository;

import com.cosium.prescription.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // BUG: This query causes N+1 problem - no JOIN FETCH for patient and orderItems
    // Also filters in memory instead of using SQL WHERE clause
    List<Order> findAll();
}

