package com.example.warehousemanagement.mapper.impl;

import com.example.warehousemanagement.dto.ItemOrderDto;
import com.example.warehousemanagement.dto.OrderResponseDto;
import com.example.warehousemanagement.dto.UserProfileDto;
import com.example.warehousemanagement.mapper.OrderMapper;
import com.example.warehousemanagement.model.Order;
import com.example.warehousemanagement.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapperImpl implements OrderMapper {

    private final ModelMapper mapper;

    @Override
    public OrderResponseDto toDto(Order order) {

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setOrderId(order.getOrderId());
        orderResponseDto.setOrderNumber(order.getOrderNumber());
        orderResponseDto.setDeadlineDate(order.getDeadlineDate());
        orderResponseDto.setUser(mapper.map(order.getUser(), UserProfileDto.class));
        orderResponseDto.setSubmittedDate(order.getSubmittedDate());
        orderResponseDto.setOrderStatus(order.getOrderStatus());

        List<ItemOrderDto> itemOrdersDto = new ArrayList<>();
        if(order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            for(OrderItem orderItem : order.getOrderItems()) {
                ItemOrderDto itemOrderDto = new ItemOrderDto();
                itemOrderDto.setOrderItemId(orderItem.getOrderItemId());
                itemOrderDto.setItemName(orderItem.getItem().getItemName());
                itemOrderDto.setOrderQuantity(orderItem.getOrderQuantity());
                itemOrderDto.setItemPrice(orderItem.getItem().getUnitPrice());

                itemOrdersDto.add(itemOrderDto);
            }
        }
        orderResponseDto.setOrderItems(itemOrdersDto);

        return orderResponseDto;
    }
}
