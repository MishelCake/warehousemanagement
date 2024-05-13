package com.example.warehousemanagement.repository;

import com.example.warehousemanagement.model.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {
    ResetPassword findByToken(String token);
    Optional<ResetPassword> findByUser_Email(String email);
}
