package com.example.warehousemanagement.dto;

import com.example.warehousemanagement.enums.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private String name;

    private String surname;

    private String address;

    private Integer postalCode;

    private String phoneNumber;

    private UserRoles role;

    private LocalDateTime createdDate;
}
