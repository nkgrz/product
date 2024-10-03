package ru.buynest.product.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.buynest.product.api.request.SaveOrUpdateProductRequest;
import ru.buynest.product.api.response.ProductResponse;
import ru.buynest.product.dao.ProductDao;
import ru.buynest.product.exception.product.ProductNotFoundException;
import ru.buynest.product.model.Product;

import java.util.UUID;

import static ru.buynest.product.service.converter.CategoryConverter.getCategoryListFromListCategoryResponse;
import static ru.buynest.product.service.converter.ProductConverter.productToProductResponse;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional
    public ProductResponse createProduct(SaveOrUpdateProductRequest requestEntity) {
        UUID uuid = UUID.randomUUID();

        Product product = new Product(
                uuid,
                requestEntity.getName(),
                requestEntity.getDescription(),
                requestEntity.getPrice(),
                requestEntity.getStock(),
                requestEntity.getImages(),
                requestEntity.getSpecifications(),
                requestEntity.getCreatedAt(),
                requestEntity.getUpdatedAt(),
                requestEntity.isActive());

        product.setCategories(getCategoryListFromListCategoryResponse(requestEntity.getCategories()));

        productDao.upsert(product);
        return productToProductResponse(product);
    }

    @Transactional
    public ProductResponse updateProduct(UUID productId, SaveOrUpdateProductRequest requestEntity) {
        Product product = getProductByIdOrThrow(productId);

        product.setName(requestEntity.getName());
        product.setDescription(requestEntity.getDescription());
        product.setPrice(requestEntity.getPrice());
        product.setStock(requestEntity.getStock());
        product.setImages(requestEntity.getImages());
        product.setSpecifications(requestEntity.getSpecifications());
        product.setUpdatedAt(requestEntity.getUpdatedAt());
        product.setActive(requestEntity.isActive());
        product.setCategories(getCategoryListFromListCategoryResponse(requestEntity.getCategories()));
        productDao.upsert(product);
        return productToProductResponse(product);
    }

    public ProductResponse getProductById(UUID productId) {
        Product product = getProductByIdOrThrow(productId);
        return productToProductResponse(product);
    }

    private Product getProductByIdOrThrow(UUID productId) {
        Product product = productDao.findById(productId);
        if (product == null) {
            throw new ProductNotFoundException(productId);
        }
        return product;
    }
}
