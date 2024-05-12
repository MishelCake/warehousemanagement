package com.example.warehousemanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;

@Table(name = "trucks")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Truck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "truck_id")
    private Long truckId;

    @Column(name = "chassis_number", unique = true)
    private String chassisNumber;

    @Column(name = "license_plate", unique = true)
    private String licensePlate;

    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "delivery")
    private Delivery delivery;
}
