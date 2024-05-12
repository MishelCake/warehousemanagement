package com.example.warehousemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    @NotNull(message = "Id must be provided")
    private Long userId;

    @Size(max = 100, message = "Name length must be less than 100 characters")
    @NotBlank(message = "Name can not be empty")
    private String name;

    @Size(max = 100, message = "Surname length must be less than 100 characters")
    @NotBlank(message = "Name can not be empty")
    private String surname;

    private String address;

    private Integer postalCode;

    private String phoneNumber;
}
