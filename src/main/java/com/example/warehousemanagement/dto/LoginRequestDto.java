package com.example.warehousemanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    @NotBlank(message = "Email can not be empty")
    @Email(message = "Email not valid")
    @Size(max = 100, message = "Email length must be less than 100 characters")
    private String email;

    @NotBlank(message = "Password can not be null")
    private String password;
}
