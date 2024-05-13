package com.example.warehousemanagement.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class WarehouseException extends RuntimeException{

    private final List<String> messages;

    public WarehouseException(String message) {
        super(message);
        this.messages = null;
    }

    public WarehouseException(List<String> messages) {
        super("");
        this.messages = messages;
    }

    public WarehouseException(String message, List<String> messages) {
        super(message);
        this.messages = messages;
    }

    public static WarehouseException notFoundById(String className, Object id) {
        String message = String.format("%s with Id %s not found", className, id.toString());

        return new WarehouseException(message);
    }
}
