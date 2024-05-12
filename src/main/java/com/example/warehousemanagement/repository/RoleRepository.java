package com.example.warehousemanagement.repository;

import com.example.warehousemanagement.enums.UserRoles;
import com.example.warehousemanagement.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(UserRoles role);
}
