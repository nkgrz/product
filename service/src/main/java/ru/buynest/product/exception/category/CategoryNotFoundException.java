package ru.buynest.product.exception.category;

import org.springframework.http.HttpStatus;
import ru.buynest.product.exception.BusinessException;

import java.util.UUID;

public class CategoryNotFoundException extends BusinessException {

    public CategoryNotFoundException(UUID categoryId) {
        super(String.format("Category with ID %s not found", categoryId),
                "CATEGORY_NOT_FOUND",
                HttpStatus.NOT_FOUND);
    }
}