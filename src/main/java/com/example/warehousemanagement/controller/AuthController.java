package com.example.warehousemanagement.controller;

import com.example.warehousemanagement.dto.LoginRequestDto;
import com.example.warehousemanagement.dto.ServiceResponse;
import com.example.warehousemanagement.dto.UserDto;
import com.example.warehousemanagement.service.UserService;
import com.example.warehousemanagement.util.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ServiceResponse> login(@Valid @RequestBody LoginRequestDto authRequest) {
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, userService.login(authRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<ServiceResponse> register(@Valid @RequestBody UserDto userDto) {
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, userService.register(userDto));
    }

}
