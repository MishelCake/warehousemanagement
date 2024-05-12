package com.example.warehousemanagement.repository;

import com.example.warehousemanagement.model.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TruckRepository extends JpaRepository<Truck, Long> {
    List<Truck> findAllByEnabledTrue();

    Boolean existsByChassisNumber(String chassisNumber);

    Boolean existsByLicensePlate(String licensePlate);

    Optional<Truck> findTruckByChassisNumber(String chassisNumber);
}
