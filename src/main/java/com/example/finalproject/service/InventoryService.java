package com.example.finalproject.service;

import com.example.finalproject.model.Inventory;
import com.example.finalproject.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public Optional<Inventory> getInventoryById(Long id) {
        return inventoryRepository.findById(id);
    }

    public Inventory createInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    public Optional<Inventory> updateInventory(Long id, Inventory inventory) {
        return inventoryRepository.findById(id)
                .map(existingInventory -> {
                    existingInventory.setInstrument(inventory.getInstrument());
                    existingInventory.setQuantity(inventory.getQuantity());
                    return inventoryRepository.save(existingInventory);
                });
    }

    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }
}
