package com.example.finalproject.controller;

import com.example.finalproject.model.Category;
import com.example.finalproject.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String showCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "categories"; // Replace with your template name
    }

    @GetMapping("/{id}")
    public String showCategory(@PathVariable Long id, Model model) {
        Optional<Category> category = categoryService.getCategoryById(id);
        category.ifPresent(model::addAttribute);
        return "categoryDetails"; // Replace with your template name
    }

    @GetMapping("/create")
    public String showCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "createCategory"; // Replace with your template name
    }

    @PostMapping("/create")
    public String createCategory(@ModelAttribute("category") Category category, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "createCategory";
        }
        try {
            categoryService.createCategory(category);
            return "redirect:/categories";
        } catch (Exception e) {
            log.error("Error creating category: ", e); // Log the error for debugging
            model.addAttribute("error", "Error creating category: " + e.getMessage());
            return "createCategory";
        }
    }


    @GetMapping("/{id}/edit")
    public String showCategoryEditForm(@PathVariable Long id, Model model) {
        Optional<Category> category = categoryService.getCategoryById(id);
        category.ifPresent(model::addAttribute);
        return "editCategory"; // Replace with your template name
    }

    @PostMapping("/{id}/edit")
    public String updateCategory(@PathVariable Long id, @ModelAttribute("category") Category category, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "editCategory";
        }
        try {
            categoryService.updateCategory(id, category);
            return "redirect:/categories";
        } catch (Exception e) {
            log.error("Error updating category: ", e); // Log the error for debugging
            model.addAttribute("error", "Error updating category: " + e.getMessage());
            model.addAttribute("category", category);
            return "editCategory";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/categories";
    }
}