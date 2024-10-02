package ru.buynest.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.buynest.product.dao.CategoryDao;
import ru.buynest.product.exception.category.CategoryExistException;
import ru.buynest.product.exception.category.CategoryNotFoundException;
import ru.buynest.product.model.Category;
import ru.buynest.product.api.request.SaveOrUpdateCategoryRequest;
import ru.buynest.product.api.response.CategoryResponse;

import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryDao categoryDao;

    @Autowired
    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Transactional
    public CategoryResponse createCategory(SaveOrUpdateCategoryRequest request) {

        String categoryName = request.getName();
        UUID parentCategoryId = request.getParentCategoryId();
        if (!categoryDao.findByNameAndParentCategoryId(categoryName, parentCategoryId).isEmpty()) {
            throw new CategoryExistException(categoryName, parentCategoryId);
        }

        UUID uuid = UUID.randomUUID();
        Category category = new Category(
                uuid,
                request.getName(),
                request.getDescription(),
                request.getParentCategoryId(),
                request.getCreatedAt(),
                request.getUpdatedAt());
        categoryDao.upsert(category);
        return categoryToCategoryResponse(category);
    }

    @Transactional
    public CategoryResponse updateCategory(UUID categoryId, SaveOrUpdateCategoryRequest request) {
        Category category = getCategoryByIdOrThrow(categoryId);

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setParentCategoryId(request.getParentCategoryId());
        category.setUpdatedAt(request.getUpdatedAt());
        categoryDao.upsert(category);
        return categoryToCategoryResponse(category);
    }

    public CategoryResponse getCategoryById(UUID categoryId) {
        Category category = getCategoryByIdOrThrow(categoryId);
        return categoryToCategoryResponse(category);
    }

    private CategoryResponse categoryToCategoryResponse(Category category) {
        return new CategoryResponse(category.getId(),
                category.getName(),
                category.getDescription(),
                category.getParentCategoryId(),
                category.getCreatedAt(),
                category.getUpdatedAt());
    }

    private Category getCategoryByIdOrThrow(UUID categoryId) {
        Optional<Category> categoryOptional = categoryDao.findById(categoryId);
        if (categoryOptional.isEmpty()) {
            throw new CategoryNotFoundException(categoryId);
        }
        return categoryOptional.get();
    }
}
