package com.example.warehousemanagement.service.impl;

import com.example.warehousemanagement.dto.TruckDto;
import com.example.warehousemanagement.exception.WarehouseException;
import com.example.warehousemanagement.model.Truck;
import com.example.warehousemanagement.repository.TruckRepository;
import com.example.warehousemanagement.service.TruckService;
import com.example.warehousemanagement.util.Constants;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TruckServiceImpl implements TruckService {
    private final TruckRepository truckRepository;
    private final ModelMapper mapper;

    @Override
    public List<TruckDto> getAllActiveTrucks() {
        List<Truck> activeTrucks = truckRepository.findAllByEnabledTrue();

        return activeTrucks.stream().map(truck -> mapper.map(truck, TruckDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TruckDto getTruckById(Long truckId) {
        Truck truck = truckRepository.findById(truckId).orElse(null);
        if(truck == null) {
            throw new WarehouseException(Constants.TRUCK_NOT_FOUND);
        }
        return mapper.map(truck, TruckDto.class);
    }

    @Override
    public TruckDto addTruck(TruckDto truckDto) {
        if (truckRepository.existsByChassisNumber(truckDto.getChassisNumber())) {
            throw new WarehouseException(Constants.CHASSIS_NUMBER_EXISTS);
        }
        if (truckRepository.existsByLicensePlate(truckDto.getLicensePlate())) {
            throw new WarehouseException(Constants.LICENSE_PLATE_EXISTS);
        }
        Truck truck = mapper.map(truckDto, Truck.class);
        truck.setEnabled(Boolean.TRUE);

        truckRepository.save(truck);

        return mapper.map(truck, TruckDto.class);
    }

    @Override
    public TruckDto updateLicensePlate(String chassisNumber, String newLicensePlate) {
        Truck truck = truckRepository.findTruckByChassisNumber(chassisNumber).orElse(null);
        if(truck == null) {
            throw new WarehouseException(Constants.TRUCK_NOT_FOUND);
        }
        if (truckRepository.existsByLicensePlate(newLicensePlate)) {
            throw new WarehouseException(Constants.LICENSE_PLATE_EXISTS);
        }
        truck.setLicensePlate(newLicensePlate);
        truckRepository.save(truck);

        return mapper.map(truck, TruckDto.class);
    }

    @Override
    public void disableTruck(String chassisNumber) {
        Truck truck = truckRepository.findTruckByChassisNumber(chassisNumber).orElse(null);

        if(truck == null) {
            throw new WarehouseException(Constants.TRUCK_NOT_FOUND);
        }
        truck.setEnabled(Boolean.FALSE);
        truckRepository.save(truck);
    }
}
