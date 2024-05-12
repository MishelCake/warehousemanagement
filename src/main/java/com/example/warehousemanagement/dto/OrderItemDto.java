package com.example.warehousemanagement.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private Long itemId;
    @PositiveOrZero(message = "Quantity must be a positive number")
    private Integer quantity;
}
