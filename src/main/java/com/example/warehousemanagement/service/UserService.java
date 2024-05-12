package com.example.warehousemanagement.service;

import com.example.warehousemanagement.dto.LoginRequestDto;
import com.example.warehousemanagement.dto.LoginResponseDto;
import com.example.warehousemanagement.dto.UpdateUserDto;
import com.example.warehousemanagement.dto.UserDto;
import com.example.warehousemanagement.dto.UserProfileDto;

import java.util.List;

public interface UserService {
    LoginResponseDto login(LoginRequestDto authRequest);

    UserDto register(UserDto userDto);

    List<UserProfileDto> getAllUsers();

    UserProfileDto updateUser(UpdateUserDto userDto);

    void disableUser(Long userId);
}
