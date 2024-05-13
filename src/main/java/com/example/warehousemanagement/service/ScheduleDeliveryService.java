package com.example.warehousemanagement.service;

import com.example.warehousemanagement.dto.ScheduleDeliveryDto;

public interface ScheduleDeliveryService {
    void scheduleDelivery(ScheduleDeliveryDto scheduleDeliveryDto);

    void fulfillOrder(Long deliveryId);
}
