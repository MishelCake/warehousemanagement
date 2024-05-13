package com.example.warehousemanagement.controller;

import com.example.warehousemanagement.dto.OrderDto;
import com.example.warehousemanagement.dto.OrderItemDto;
import com.example.warehousemanagement.dto.ServiceResponse;
import com.example.warehousemanagement.dto.UpdateOrderDto;
import com.example.warehousemanagement.enums.OrderStatus;
import com.example.warehousemanagement.service.OrderService;
import com.example.warehousemanagement.util.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order/create")
    public ResponseEntity<ServiceResponse> createOrder(@Valid @RequestBody OrderDto orderDto) {
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, orderService.createOrder(orderDto));
    }

    @PostMapping("/order/add/items")
    public ResponseEntity<ServiceResponse> addItemsToOrder(@Valid @RequestBody UpdateOrderDto updateOrderDto) {
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, orderService.addItemsToOrder(updateOrderDto));
    }

    @PostMapping("/order/remove/items")
    public ResponseEntity<ServiceResponse> removeItemsToOrder(@Valid @RequestParam Long orderId, @RequestBody List<Long> itemsToRemove) {
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, orderService.removeItemsFromOrder(orderId, itemsToRemove));
    }

    @PutMapping("/order/modify/items")
    public ResponseEntity<ServiceResponse> modifyOrderItems(@Valid @RequestParam Long orderId, @Valid @RequestBody List<OrderItemDto> modifiedItems) {
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, orderService.modifyQuantityOfOrderItems(orderId, modifiedItems));
    }

    @PutMapping("/order/cancel")
    public ResponseEntity<ServiceResponse> cancelOrder(@Valid @RequestParam Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK);
    }

    @PutMapping("/order/submit")
    public ResponseEntity<ServiceResponse> submitOrder(@Valid @RequestParam Long orderId) {
        orderService.submitOrder(orderId);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK);
    }

    @GetMapping( "/list/orders")
    public ResponseEntity<ServiceResponse> getUserOrders(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "deadlineDate") String sortBy,
            @RequestParam(required = false, name = "orderStatus") OrderStatus orderStatus) {

        return ResponseHandler.generateResponse("OK", HttpStatus.OK, orderService.getAllOrdersOfLoggedInUser(orderStatus, pageNo, pageSize, sortBy));
    }

    @GetMapping("/manager/view-orders")
    public ResponseEntity<ServiceResponse> getAllOrders(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "5") Integer pageSize,
            @RequestParam(defaultValue = "submittedDate") String sortBy,
            @RequestParam(required = false, name = "orderStatus") OrderStatus orderStatus) {

        return ResponseHandler.generateResponse("OK", HttpStatus.OK, orderService.getAllOrders(orderStatus, pageNo, pageSize, sortBy));
    }

    @GetMapping("/order-details")
    public ResponseEntity<ServiceResponse> getOrderById(@Valid @RequestParam Long orderId) {
        return ResponseHandler.generateResponse("OK", HttpStatus.OK, orderService.getOrderById(orderId));
    }

    @PutMapping("/order/approve")
    public ResponseEntity<ServiceResponse> approveOrder(@Valid @RequestParam Long orderId) {
        orderService.approveOrder(orderId);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK);
    }

    @PutMapping(value = "/order/decline")
    public ResponseEntity<ServiceResponse> declineOrder(
            @RequestParam Long orderId,
            @RequestParam String message) {

        orderService.declineOrder(orderId, message);
        return ResponseHandler.generateResponse("OK", HttpStatus.OK);
    }
}
