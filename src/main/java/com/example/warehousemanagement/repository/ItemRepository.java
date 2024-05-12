package com.example.warehousemanagement.repository;

import com.example.warehousemanagement.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByEnabledTrue();
    Boolean existsByItemName(String itemName);
}
