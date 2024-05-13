package com.example.warehousemanagement.controller;

import com.example.warehousemanagement.dto.ItemDto;
import com.example.warehousemanagement.dto.ServiceResponse;
import com.example.warehousemanagement.service.ItemService;
import com.example.warehousemanagement.util.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/all")
    public ResponseEntity<ServiceResponse> getAllActiveItems() {
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, itemService.getAllActiveItems());
    }

    @GetMapping("/id")
    public ResponseEntity<ServiceResponse> getItemById(@Valid @RequestParam Long itemId) {
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, itemService.getItemById(itemId));
    }

    @PostMapping("/add")
    public ResponseEntity<ServiceResponse> addNewItem(@Valid @RequestBody ItemDto itemDto) {
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, itemService.addItem(itemDto));
    }

    @PutMapping("/update")
    public ResponseEntity<ServiceResponse> updateItem(@Valid @RequestBody ItemDto itemDto) {
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, itemService.updateItem(itemDto));
    }

    @PutMapping("/disable")
    public ResponseEntity<ServiceResponse> disableUser(@Valid @RequestParam Long itemId) {
        itemService.disableItem(itemId);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK);
    }
}
