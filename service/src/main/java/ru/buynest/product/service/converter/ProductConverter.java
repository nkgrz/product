package ru.buynest.product.service.converter;

import ru.buynest.product.api.response.ProductResponse;
import ru.buynest.product.model.Product;

import static ru.buynest.product.service.converter.CategoryConverter.getCategoryResponseListFromCategoryList;

public class ProductConverter {

    public static ProductResponse productToProductResponse(Product product) {

        ProductResponse productResponse = new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImages(),
                product.getSpecifications(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.isActive()
        );

        productResponse.setCategories(getCategoryResponseListFromCategoryList(product.getCategories()));

        return productResponse;
    }

}
