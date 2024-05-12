package com.example.warehousemanagement.repository;

import com.example.warehousemanagement.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    boolean existsByOrder_OrderIdAndItem_ItemId(Long orderId, Long itemId);
    Optional<OrderItem> findOrderItemByOrder_OrderIdAndItem_ItemId(Long orderId, Long itemId);
}
