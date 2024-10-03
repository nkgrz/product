package ru.buynest.product.service.converter;

import ru.buynest.product.api.response.CategoryResponse;
import ru.buynest.product.model.Category;

import java.util.ArrayList;
import java.util.List;


public class CategoryConverter {

    public static CategoryResponse categoryToCategoryResponse(Category category) {
        return new CategoryResponse(category.getId(),
                category.getName(),
                category.getDescription(),
                category.getParentCategoryId(),
                category.getCreatedAt(),
                category.getUpdatedAt());
    }

    public static Category categoryResponseToCategory(CategoryResponse categoryResponse) {
        return new Category(
                categoryResponse.getId(),
                categoryResponse.getName(),
                categoryResponse.getDescription(),
                categoryResponse.getParentCategoryId(),
                categoryResponse.getCreatedAt(),
                categoryResponse.getUpdatedAt()
        );
    }

    public static List<Category> getCategoryListFromListCategoryResponse(List<CategoryResponse> listCategoryResponse) {
        if (listCategoryResponse != null) {
            List<Category> categories = new ArrayList<>();
            for (CategoryResponse categoryResponse : listCategoryResponse) {
                categories.add(categoryResponseToCategory(categoryResponse));
            }
            return categories;
        } else {
            return null;
        }
    }

    public static List<CategoryResponse> getCategoryResponseListFromCategoryList(List<Category> categories) {
        if (categories != null) {
            List<CategoryResponse> categoryResponses = new ArrayList<>();
            for (Category category : categories) {
                categoryResponses.add(categoryToCategoryResponse(category));
            }
            return categoryResponses;
        } else {
            return null;
        }
    }
}
