package com.example.warehousemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemOrderDto {
    private Long orderItemId;
    private Integer orderQuantity;
    private String itemName;
    private Double itemPrice;
}
