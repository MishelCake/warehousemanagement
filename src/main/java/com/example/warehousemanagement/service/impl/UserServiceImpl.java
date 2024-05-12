package com.example.warehousemanagement.service.impl;

import com.example.warehousemanagement.dto.LoginRequestDto;
import com.example.warehousemanagement.dto.LoginResponseDto;
import com.example.warehousemanagement.dto.UpdateUserDto;
import com.example.warehousemanagement.dto.UserDto;
import com.example.warehousemanagement.dto.UserProfileDto;
import com.example.warehousemanagement.enums.UserRoles;
import com.example.warehousemanagement.exception.WarehouseException;
import com.example.warehousemanagement.model.Role;
import com.example.warehousemanagement.model.User;
import com.example.warehousemanagement.repository.RoleRepository;
import com.example.warehousemanagement.repository.UserRepository;
import com.example.warehousemanagement.security.JwtTokenProvider;
import com.example.warehousemanagement.security.SecurityUserDetailsService;
import com.example.warehousemanagement.security.UserDetailsImpl;
import com.example.warehousemanagement.security.exception.UnauthorizedException;
import com.example.warehousemanagement.service.UserService;
import com.example.warehousemanagement.util.Constants;
import com.example.warehousemanagement.util.LoggedInUserInfo;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;
    private final SecurityUserDetailsService userDetailService;
    private final JwtTokenProvider jwtProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final LoggedInUserInfo loggedInUserInfo;

    @Override
    public LoginResponseDto login(LoginRequestDto authRequest) {
        Authentication auth;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (Exception e) {
            throw new UnauthorizedException(e.getMessage());
        }

        SecurityContextHolder.getContext().setAuthentication(auth);
        UserDetailsImpl user = (UserDetailsImpl) userDetailService.loadUserByUsername(auth.getName());
        User savedUser = userRepository.findById(user.getUserId()).get();
        UserDto userDetails = mapper.map(savedUser, UserDto.class);

        Optional<String> authority = user.getAuthorities().stream().findFirst().map(Object::toString);
        String result = authority.map(s -> s.replaceFirst("^ROLE_", "")).orElse("");

        userDetails.setRole(UserRoles.valueOf(result));

        return new LoginResponseDto(
                jwtProvider.generateToken(user));
    }

    @Override
    public UserDto register(UserDto userDto) {
        if (userDto.getUserId() == null && !userRepository.existsByEmail(userDto.getEmail())) {

            User user = mapper.map(userDto, User.class);
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setCreatedDate(LocalDateTime.now());
            user.setEnabled(Boolean.TRUE);
            Role role = roleRepository.findByRole(userDto.getRole())
                    .orElseThrow(() -> new ValidationException(Constants.ROLE_NOT_FOUND));
            user.setRole(role);
            user = userRepository.save(user);

            return mapper.map(user, UserDto.class);
        }
        throw new ValidationException("User with email ".concat(userDto.getEmail()).concat(" exists"));
    }

    @Override
    public List<UserProfileDto> getAllUsers() {
        List<User> users = userRepository.findAllByEnabledTrue();

        return users.stream()
                .map(user -> mapper.map(user, UserProfileDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserProfileDto updateUser(UpdateUserDto userDto) {
        User user = userRepository.findById(userDto.getUserId()).orElse(null);
        if (user == null) {
            throw new WarehouseException(Constants.USER_NOT_FOUND);
        }
        if (user.getRole().getRole().equals(UserRoles.SYSTEM_ADMIN) && !loggedInUserInfo.getLoggedInUserEmail().equals(user.getEmail())) {
            throw new WarehouseException(Constants.NOT_AUTHORIZED);
        }

        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setAddress(userDto.getAddress());
        user.setPostalCode(userDto.getPostalCode());
        user.setPhoneNumber(userDto.getPhoneNumber());

        userRepository.save(user);

        return mapper.map(user, UserProfileDto.class);
    }

    @Override
    public void disableUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null) {
            throw new WarehouseException(Constants.USER_NOT_FOUND);
        }
        if (user.getRole().getRole().equals(UserRoles.SYSTEM_ADMIN)) {
            throw new WarehouseException(Constants.NOT_AUTHORIZED);
        }
        user.setEnabled(false);
        userRepository.save(user);
    }
}
