package com.example.warehousemanagement.util;

import com.example.warehousemanagement.dto.ServiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    private ResponseHandler() {

    }

    public static ResponseEntity<ServiceResponse> generateResponse(String message, HttpStatus status, Object responseObj) {
        ServiceResponse response = new ServiceResponse(status.value(), message, responseObj);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<ServiceResponse> generateResponse(String message, HttpStatus status) {
        ServiceResponse response = new ServiceResponse(status.value(), message, null);
        return new ResponseEntity<>(response, status);
    }

    public static ResponseEntity<Object> generateObjectResponse(String message, HttpStatus status, Object object) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("status", status.value());
        map.put("data", object);
        return new ResponseEntity<>(map, status);
    }
}
