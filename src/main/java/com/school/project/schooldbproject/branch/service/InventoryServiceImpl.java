package com.school.project.schooldbproject.branch.service;

import com.school.project.schooldbproject.branch.dto.CreateInventoryDto;
import com.school.project.schooldbproject.branch.entity.Inventory;
import com.school.project.schooldbproject.branch.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }


    @Override
    public Inventory createInventory(CreateInventoryDto createInventoryDto) {
        return null;
    }
}