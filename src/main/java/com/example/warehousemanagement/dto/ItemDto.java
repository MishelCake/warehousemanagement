package com.example.warehousemanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    private Long itemId;

    @NotBlank(message = "Item name must not be empty")
    private String itemName;

    @NotNull(message = "Quantity must not be null")
    private Integer quantity;

    @NotNull(message = "Unit price must not be null")
    private Double unitPrice;

    private Boolean enabled;
}
