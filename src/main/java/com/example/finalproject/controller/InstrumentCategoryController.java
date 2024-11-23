package com.example.finalproject.controller;

import com.example.finalproject.model.InstrumentCategory;
import com.example.finalproject.service.InstrumentCategoryService;
import com.example.finalproject.service.InstrumentService;
import com.example.finalproject.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/instrumentCategories")
public class InstrumentCategoryController {

    @Autowired
    private InstrumentCategoryService instrumentCategoryService;
    @Autowired
    private InstrumentService instrumentService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String showInstrumentCategories(Model model) {
        model.addAttribute("instrumentCategories", instrumentCategoryService.findAll());
        model.addAttribute("instruments", instrumentService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "instrumentCategories"; // Your Thymeleaf template name
    }

    @GetMapping("/{id}")
    public String showInstrumentCategoryDetails(@PathVariable Long id, Model model) {
        InstrumentCategory instrumentCategory = instrumentCategoryService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instrument Category not found"));
        model.addAttribute("instrumentCategory", instrumentCategory);
        return "instrumentCategoryDetails"; //Replace with your template name
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("instruments", instrumentService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "instrumentCategories"; // Use the same template for both
    }


    @PostMapping("/create")
    public String createInstrumentCategory(@RequestParam Long instrumentId, @RequestParam Long categoryId, Model model) {
        try {
            instrumentCategoryService.create(instrumentId, categoryId);
            return "redirect:/instrumentCategories";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("instruments", instrumentService.findAll());
            model.addAttribute("categories", categoryService.findAll());
            return "instrumentCategories"; // Use the same template
        }
    }


    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
            InstrumentCategory instrumentCategory = instrumentCategoryService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Instrument Category not found"));

            model.addAttribute("instrumentCategory", instrumentCategory); //This line was likely missing or incorrect
            model.addAttribute("instruments", instrumentService.findAll());
            model.addAttribute("categories", categoryService.findAll());
            return "updateInstrumentCategory";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/instrumentCategories";
        } catch (Exception e) { //Catch any unexpected exceptions
            model.addAttribute("errorMessage", "An unexpected error occurred.");
            return "redirect:/instrumentCategories";
        }
    }

    @PostMapping("/update/{id}")
    public String updateInstrumentCategory(@PathVariable Long id, @ModelAttribute InstrumentCategory instrumentCategory, Model model) {
        try {
            instrumentCategoryService.update(instrumentCategory); //Modify your service to take instrumentCategory as parameter.
            return "redirect:/instrumentCategories";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("instrumentCategory", instrumentCategory); // Add the updated instrumentCategory to display again
            model.addAttribute("instruments", instrumentService.findAll());
            model.addAttribute("categories", categoryService.findAll());
            return "updateInstrumentCategory";
        }
    }


    @PostMapping("/delete/{id}")
    public String deleteInstrumentCategory(@PathVariable Long id, Model model) {
        try {
            instrumentCategoryService.delete(id);
            return "redirect:/instrumentCategories";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/instrumentCategories";
        }
    }
}