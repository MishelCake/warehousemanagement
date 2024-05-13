package com.example.warehousemanagement.controller;

import com.example.warehousemanagement.dto.LoginRequestDto;
import com.example.warehousemanagement.dto.ServiceResponse;
import com.example.warehousemanagement.dto.UserDto;
import com.example.warehousemanagement.service.UserService;
import com.example.warehousemanagement.util.ResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping(value = "/forgot-password")
    public ResponseEntity<ServiceResponse> forgotPassword(@RequestParam(name = "email") String email, HttpServletRequest httpRequest) {
        userService.forgotPassword(httpRequest, email);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK);
    }

    @PostMapping(value = "/reset-password")
    public ResponseEntity<ServiceResponse> resetPassword(@RequestParam String token, String newPassword) {
        userService.resetPassword(token, newPassword);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK);
    }

}
