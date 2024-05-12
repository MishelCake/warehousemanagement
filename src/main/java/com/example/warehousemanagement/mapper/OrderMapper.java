package com.example.warehousemanagement.mapper;

import com.example.warehousemanagement.dto.OrderResponseDto;
import com.example.warehousemanagement.model.Order;

public interface OrderMapper {
    OrderResponseDto toDto(Order order);
}
