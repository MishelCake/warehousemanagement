package com.example.warehousemanagement.service.impl;

import com.example.warehousemanagement.dto.ItemDto;
import com.example.warehousemanagement.exception.WarehouseException;
import com.example.warehousemanagement.model.Item;
import com.example.warehousemanagement.repository.ItemRepository;
import com.example.warehousemanagement.service.ItemService;
import com.example.warehousemanagement.util.Constants;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ModelMapper mapper;

    @Override
    public ItemDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElse(null);
        if(item == null) {
            throw new WarehouseException(Constants.ITEM_NOT_FOUND);
        }
        return mapper.map(item, ItemDto.class);
    }

    @Override
    public List<ItemDto> getAllActiveItems() {
        List<Item> activeItems = itemRepository.findAllByEnabledTrue();

        return activeItems.stream().map(item -> mapper.map(item, ItemDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto addItem(ItemDto itemDto) {
        if (itemRepository.existsByItemName(itemDto.getItemName())) {
            throw new WarehouseException(Constants.ITEM_ALREADY_EXISTS);
        }

        Item item = mapper.map(itemDto, Item.class);
        item.setItemId(null);
        item.setEnabled(Boolean.TRUE);

        itemRepository.save(item);

        return mapper.map(item, ItemDto.class);
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto) {

        Item item = itemRepository.findById(itemDto.getItemId()).orElse(null);
        if (item == null) {
            throw new WarehouseException(Constants.ITEM_NOT_FOUND);
        }
        if (itemRepository.existsByItemName(itemDto.getItemName())) {
            throw new WarehouseException(Constants.ITEM_ALREADY_EXISTS);
        }
        item = mapper.map(itemDto, Item.class);
        itemRepository.save(item);

        return mapper.map(item, ItemDto.class);
    }

    @Override
    public void disableItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElse(null);
        if(item == null) {
            throw new WarehouseException(Constants.ITEM_NOT_FOUND);
        }
        item.setEnabled(Boolean.FALSE);
        itemRepository.save(item);
    }


}
