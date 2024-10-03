package ru.buynest.product.exception.product;

import org.springframework.http.HttpStatus;
import ru.buynest.product.exception.BusinessException;

import java.util.UUID;

public class ProductNotFoundException extends BusinessException {

    public ProductNotFoundException(UUID productId) {
        super(String.format("Product with ID %s not found", productId),
                "PRODUCT_NOT_FOUND",
                HttpStatus.NOT_FOUND);
    }
}
