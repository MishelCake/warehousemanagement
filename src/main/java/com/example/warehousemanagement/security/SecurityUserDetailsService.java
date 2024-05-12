package com.example.warehousemanagement.security;

import com.example.warehousemanagement.model.User;
import com.example.warehousemanagement.repository.UserRepository;
import com.example.warehousemanagement.security.exception.UnauthorizedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot load user details: User Not Found"));
        if (user.getEnabled().equals(Boolean.FALSE)) {
            throw new UnauthorizedException("Bad credentials");
        }
        return UserDetailsImpl.build(user);
    }
}
