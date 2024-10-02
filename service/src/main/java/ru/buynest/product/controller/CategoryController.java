package ru.buynest.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.buynest.product.service.CategoryService;
import ru.buynest.product.api.request.SaveOrUpdateCategoryRequest;
import ru.buynest.product.api.response.CategoryResponse;

import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestBody SaveOrUpdateCategoryRequest request) {
        CategoryResponse createdCategory = categoryService.createCategory(request);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable UUID categoryId,
            @RequestBody SaveOrUpdateCategoryRequest request) {
        CategoryResponse updatedCategory = categoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(updatedCategory);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }

}