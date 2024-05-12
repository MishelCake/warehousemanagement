package com.example.warehousemanagement.dto;

import com.example.warehousemanagement.enums.UserRoles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long userId;

    @Size(max = 100, message = "Name length must be less than 100 characters")
    @NotBlank(message = "Name must be provided")
    private String name;

    @Size(max = 100, message = "Surname length must be less than 100 characters")
    @NotBlank(message = "surname must be provided")
    private String surname;

    private String address;

    private Integer postalCode;

    private String phoneNumber;

    @Email(message = "Email not valid")
    @Size(max = 100, message = "Email length must be less than 100 characters")
    private String email;

    @NotNull(message = "Password not valid")
    private String password;

    private UserRoles role;

    private Boolean enabled;

    private LocalDateTime createdDate;
}
