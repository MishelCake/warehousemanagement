package com.example.warehousemanagement.dto;

import com.example.warehousemanagement.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private Long orderId;

    private String orderNumber;

    private LocalDateTime submittedDate;

    private OrderStatus orderStatus;

    private List<ItemOrderDto> orderItems;

    private LocalDateTime deadlineDate;

    private UserProfileDto user;
}
