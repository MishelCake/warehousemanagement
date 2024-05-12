package com.example.warehousemanagement.service;

public interface EmailService {
    void sendMessageWhenOrderDeclined(String email, String name, String orderNumber, String reason);
}
