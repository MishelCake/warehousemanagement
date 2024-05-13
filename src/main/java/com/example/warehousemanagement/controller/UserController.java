package com.example.warehousemanagement.controller;

import com.example.warehousemanagement.dto.ServiceResponse;
import com.example.warehousemanagement.dto.UpdateUserDto;
import com.example.warehousemanagement.service.UserService;
import com.example.warehousemanagement.util.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<ServiceResponse> getAllUsers() {
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, userService.getAllUsers());
    }

    @PutMapping("/update")
    public ResponseEntity<ServiceResponse> updateUser(@Valid @RequestBody UpdateUserDto updateUserDto) {
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, userService.updateUser(updateUserDto));
    }

    @PostMapping("/disable")
    public ResponseEntity<ServiceResponse> disableUser(@Valid @RequestParam Long userId) {
        userService.disableUser(userId);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK);
    }

}
