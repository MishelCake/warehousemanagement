package com.example.warehousemanagement.service;

import com.example.warehousemanagement.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto getItemById(Long itemId);

    List<ItemDto> getAllActiveItems();

    ItemDto addItem(ItemDto itemDto);

    ItemDto updateItem(ItemDto itemDto);

    void disableItem(Long itemId);
}
