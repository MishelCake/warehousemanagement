package com.example.warehousemanagement.controller;

import com.example.warehousemanagement.dto.ScheduleDeliveryDto;
import com.example.warehousemanagement.dto.ServiceResponse;
import com.example.warehousemanagement.service.ScheduleDeliveryService;
import com.example.warehousemanagement.util.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScheduleDeliveryController {
    private final ScheduleDeliveryService scheduleDeliveryService;

    @PostMapping("/schedule/order")
    public ResponseEntity<ServiceResponse> scheduleDelivery(ScheduleDeliveryDto scheduleDeliveryDto) {
        scheduleDeliveryService.scheduleDelivery(scheduleDeliveryDto);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK);
    }

    @PutMapping("/delivery/fulfill")
    public ResponseEntity<ServiceResponse> fulfillOrder(@Valid @RequestParam Long deliveryId) {
        scheduleDeliveryService.fulfillOrder(deliveryId);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK);
    }
}
