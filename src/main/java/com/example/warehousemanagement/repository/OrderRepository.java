package com.example.warehousemanagement.repository;

import com.example.warehousemanagement.enums.OrderStatus;
import com.example.warehousemanagement.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUserEmailAndOrderStatus(String email, OrderStatus orderStatus, Pageable pageable);
    Page<Order> findAllByUserEmail(String email, Pageable pageable);
    Page<Order> findAllByOrderStatus(OrderStatus orderStatus, Pageable pageable);
}
