package com.example.warehousemanagement.service.impl;

import com.example.warehousemanagement.exception.WarehouseException;
import com.example.warehousemanagement.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        try {
            mailSender.send(message);
        } catch (MailException e) {
            throw new WarehouseException("Email not correct");
        }
    }

    @Override
    public void sendMessageWhenOrderDeclined(String email, String name, String orderNumber, String reason) {
        String body = "Hello " + name + "!\n\n" + "Your order with order number " + orderNumber + " was canceled.\n\n"
                + reason + "\n\n" + "Thank you.";
        String subject = "Order Declined.";
        this.sendEmail(email, subject, body);
    }

    @Override
    public void sendEmailToResetPassword(String email, String token, HttpServletRequest httpRequest) {
        String body ="Click here to reset your password " + "http://localhost:8080" + httpRequest.getContextPath() + "/password/reset?token=" + token;
        String subject = "Reset password.";
        this.sendEmail(email, subject, body);
    }
}
