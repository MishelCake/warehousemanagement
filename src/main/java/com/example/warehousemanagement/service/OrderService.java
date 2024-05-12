package com.example.warehousemanagement.service;

import com.example.warehousemanagement.dto.OrderBasicInfoDto;
import com.example.warehousemanagement.dto.OrderDto;
import com.example.warehousemanagement.dto.OrderItemDto;
import com.example.warehousemanagement.dto.OrderResponseDto;
import com.example.warehousemanagement.dto.UpdateOrderDto;
import com.example.warehousemanagement.enums.OrderStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(OrderDto orderDto);

    Page<OrderResponseDto> getAllOrdersOfLoggedInUser(OrderStatus orderStatus, Integer pageNo, Integer pageSize, String sortBy);

    OrderResponseDto addItemsToOrder(UpdateOrderDto updateOrderDto);

    OrderResponseDto removeItemsFromOrder(Long orderId, List<Long> itemsToRemoveIds);

    OrderResponseDto modifyQuantityOfOrderItems(Long orderId, List<OrderItemDto> modifiedItems);

    void cancelOrder(Long orderId);

    void submitOrder(Long orderId);

    Page<OrderBasicInfoDto> getAllOrders(OrderStatus orderStatus, Integer pageNo, Integer pageSize, String sortBy);

    OrderResponseDto getOrderById(Long orderId);

    void approveOrder(Long orderId);

    void declineOrder(Long orderId, String reason);
}
