package com.example.warehousemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TruckDto {

    @NotBlank(message = "Chassis number must be provided")
    private String chassisNumber;

    @NotBlank(message = "License plate must be provided")
    private String licensePlate;

    private Boolean enabled;
}
