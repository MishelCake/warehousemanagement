package com.example.warehousemanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RemoveItemsDto {
    private Long orderId;
    private List<Long> itemsToRemove;
}
