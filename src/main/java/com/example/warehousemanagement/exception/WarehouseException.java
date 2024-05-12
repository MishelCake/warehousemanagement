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
}
