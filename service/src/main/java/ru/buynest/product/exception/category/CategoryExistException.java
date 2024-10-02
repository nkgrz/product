package ru.buynest.product.exception.category;

import org.springframework.http.HttpStatus;
import ru.buynest.product.exception.BusinessException;

import java.util.UUID;

public class CategoryExistException extends BusinessException {

    public CategoryExistException(String name, UUID parentCategoryId) {
        super(String.format("Category with name %s and parent ID %s existed!", name, parentCategoryId),
                "CATEGORY_EXISTED",
                HttpStatus.CONFLICT);
    }
}
