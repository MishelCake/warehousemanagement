package com.example.warehousemanagement.service;

import jakarta.servlet.http.HttpServletRequest;

public interface EmailService {
    void sendMessageWhenOrderDeclined(String email, String name, String orderNumber, String reason);

    void sendEmailToResetPassword(String email, String token, HttpServletRequest httpRequest);
}
