package com.example.warehousemanagement.service;

import com.example.warehousemanagement.dto.TruckDto;

import java.util.List;

public interface TruckService {
    List<TruckDto> getAllActiveTrucks();

    TruckDto getTruckById(Long truckId);

    TruckDto addTruck(TruckDto truckDto);

    TruckDto updateLicensePlate(String chassisNumber, String newLicensePlate);

    void disableTruck(String chassisNumber);
}
