package com.example.warehousemanagement.controller;

import com.example.warehousemanagement.dto.ServiceResponse;
import com.example.warehousemanagement.dto.TruckDto;
import com.example.warehousemanagement.service.TruckService;
import com.example.warehousemanagement.util.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/truck/")
@RequiredArgsConstructor
public class TruckController {
    private final TruckService truckService;

    @GetMapping("/trucks")
    public ResponseEntity<ServiceResponse> getAllActiveTrucks() {
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, truckService.getAllActiveTrucks());
    }

    @GetMapping("/truck")
    public ResponseEntity<ServiceResponse> getTruckById(@Valid @RequestParam Long truckId) {
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, truckService.getTruckById(truckId));
    }

    @PostMapping("/truck/add")
    public ResponseEntity<ServiceResponse> addNewTruck(@Valid @RequestBody TruckDto truckDto) {
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, truckService.addTruck(truckDto));
    }

    @PutMapping("/truck/update/license-plate")
    public ResponseEntity<ServiceResponse> updateTruckLicensePlate(@Valid @RequestParam String chassisNumber,
                                                                   @RequestParam String newLicensePlate) {
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, truckService.updateLicensePlate(chassisNumber, newLicensePlate));
    }

    @PutMapping("/truck/disable")
    public ResponseEntity<ServiceResponse> disableUser(@Valid @RequestParam String chassisNumber) {
        truckService.disableTruck(chassisNumber);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK);
    }
}
