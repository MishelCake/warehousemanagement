package com.example.warehousemanagement.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDto {
    private Long userId;
    private List<OrderItemDto> orderItemDto;
    private LocalDateTime deadline;
}
