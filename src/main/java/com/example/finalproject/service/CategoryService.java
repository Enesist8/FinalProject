package com.example.finalproject.service;

import com.example.finalproject.model.Category;
import com.example.finalproject.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Service
@RequestMapping("/api/category")
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }


    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> updateCategory(Long id, Category category) {
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    existingCategory.setCategoryName(category.getCategoryName());
                    return categoryRepository.save(existingCategory);
                });
    }


    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}