package com.example.finalproject.repository;

import com.example.finalproject.model.Category;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Repository
public class InMemoryCategoryDAO {
    private final List<Category> CATEGORIES = new ArrayList<>();

    public List<Category> findAllCategories() {
        return CATEGORIES;
    }

    public Category findCategoryById(Long id) {
        return CATEGORIES.stream()
                .filter(category -> category.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Category createCategory(Category category) {
        CATEGORIES.add(category);
        return category;
    }

    public Category updateCategory(Category category) {
        int categoryIndex = IntStream.range(0, CATEGORIES.size())
                .filter(i -> CATEGORIES.get(i).getId().equals(category.getId()))
                .findFirst()
                .orElse(-1);
        if (categoryIndex == -1) {
            return null;
        }
        CATEGORIES.set(categoryIndex, category);
        return category;
    }

    public void deleteCategory(Long id) {
        Category categoryToDelete = findCategoryById(id);
        if (categoryToDelete != null) {
            CATEGORIES.remove(categoryToDelete);
        }
    }
}