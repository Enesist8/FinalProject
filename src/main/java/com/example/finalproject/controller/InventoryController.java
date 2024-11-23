package com.example.finalproject.controller;


import com.example.finalproject.model.Instrument;
import com.example.finalproject.model.Inventory;
import com.example.finalproject.service.InventoryService;
import com.example.finalproject.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final InstrumentService instrumentService;

    @Autowired
    public InventoryController(InventoryService inventoryService, InstrumentService instrumentService) {
        this.inventoryService = inventoryService;
        this.instrumentService = instrumentService;
    }


    @GetMapping
    public String showInventory(Model model) {
        List<Inventory> inventory = inventoryService.getAllInventory();
        model.addAttribute("inventory", inventory);
        return "inventory"; // Replace with your template name
    }

    @GetMapping("/create")
    public String showInventoryForm(Model model) {
        model.addAttribute("inventory", new Inventory());
        List<Instrument> instruments = instrumentService.getAllInstruments(); // Get instruments
        model.addAttribute("instruments", instruments); // Add to the model
        return "createInventory";
    }

    @PostMapping("/create")
    public String createInventory(@ModelAttribute("inventory") Inventory inventory, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "createInventory";
        }
        try {
            inventoryService.createInventory(inventory);
            return "redirect:/inventory";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error creating inventory: " + e.getMessage());
            return "createInventory";
        }
    }




    @GetMapping("/{id}/edit")
    public String showInventoryEditForm(@PathVariable Long id, Model model) {
        Optional<Inventory> inventory = inventoryService.getInventoryById(id);
        inventory.ifPresent(model::addAttribute);
        List<Instrument> instruments = instrumentService.getAllInstruments(); // Crucial line added here
        model.addAttribute("instruments", instruments); // Add instruments to the model
        return "editInventory";
    }

    @PostMapping("/{id}/edit")
    public String updateInventory(@PathVariable Long id, @ModelAttribute("inventory") Inventory inventory, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "editInventory";
        }
        try {
            inventoryService.updateInventory(id, inventory);
            return "redirect:/inventory";
        } catch (Exception e) {
            // Log the error for debugging
            e.printStackTrace();
            model.addAttribute("error", "Error updating inventory: " + e.getMessage());
            model.addAttribute("inventory", inventory);
            return "editInventory";
        }
    }



    @PostMapping("/{id}/delete")
    public String deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return "redirect:/inventory";
    }


}