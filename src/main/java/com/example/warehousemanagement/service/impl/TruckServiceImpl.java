package com.example.warehousemanagement.service.impl;

import com.example.warehousemanagement.dto.TruckDto;
import com.example.warehousemanagement.exception.WarehouseException;
import com.example.warehousemanagement.model.Truck;
import com.example.warehousemanagement.repository.TruckRepository;
import com.example.warehousemanagement.service.TruckService;
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
            throw new WarehouseException("Truck not found");
        }
        return mapper.map(truck, TruckDto.class);
    }

    @Override
    public TruckDto addTruck(TruckDto truckDto) {
        if (truckRepository.existsByChassisNumber(truckDto.getChassisNumber())) {
            throw new WarehouseException("Chassis number already exists");
        }
        if (truckRepository.existsByLicensePlate(truckDto.getLicensePlate())) {
            throw new WarehouseException("License plate already exists");
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
            throw new WarehouseException("Truck with the provided chassis number does not exist");
        }
        if (truckRepository.existsByLicensePlate(newLicensePlate)) {
            throw new WarehouseException("License plate already exists");
        }
        truck.setLicensePlate(newLicensePlate);
        truckRepository.save(truck);

        return mapper.map(truck, TruckDto.class);
    }

    @Override
    public void disableTruck(String chassisNumber) {
        Truck truck = truckRepository.findTruckByChassisNumber(chassisNumber).orElse(null);

        if(truck == null) {
            throw new WarehouseException("Truck with the provided chassis number does not exist");
        }
        truck.setEnabled(Boolean.FALSE);
        truckRepository.save(truck);
    }
}
