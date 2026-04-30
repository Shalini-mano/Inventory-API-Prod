package com.inventory.service;

import com.inventory.dto.CategoryRequest;
import com.inventory.dto.CategoryResponse;
import com.inventory.model.Category;
import com.inventory.repo.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // CREATE
    public CategoryResponse create(CategoryRequest request) {

        Category category = new Category();
        category.setName(request.getName());

        Category saved = categoryRepository.save(category);

        return new CategoryResponse(saved.getId(), saved.getName());
    }

    // UPDATE
    public CategoryResponse update(Long id, CategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(request.getName());

        Category updated = categoryRepository.save(category);

        return new CategoryResponse(updated.getId(), updated.getName());
    }

    // DELETE
    public void delete(Long id) {

        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }

        categoryRepository.deleteById(id);
    }

    // GET ALL
    public List<CategoryResponse> getAll() {

        return categoryRepository.findAll()
                .stream()
                .map(cat -> new CategoryResponse(cat.getId(), cat.getName()))
                .toList();
    }

    // GET BY ID
    public CategoryResponse getById(Long id) {

        Category cat = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return new CategoryResponse(cat.getId(), cat.getName());
    }
}