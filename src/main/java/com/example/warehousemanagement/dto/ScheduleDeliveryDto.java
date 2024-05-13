package com.example.warehousemanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ScheduleDeliveryDto {
    private Long orderId;
    private LocalDate deliveryDate;
    private List<Long> truckIds;
}
