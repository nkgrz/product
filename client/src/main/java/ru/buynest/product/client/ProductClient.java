package ru.buynest.product.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.buynest.product.api.request.SaveOrUpdateProductRequest;
import ru.buynest.product.api.response.ProductResponse;

import java.util.UUID;

public class ProductClient {

    private static final String PRODUCT_URL = "/products";
    private final RestTemplate restTemplate;

    public ProductClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * POST /products
     */
    public ProductResponse createProduct(SaveOrUpdateProductRequest requestEntity) {
        return restTemplate.postForObject(PRODUCT_URL, requestEntity, ProductResponse.class);
    }

    /**
     * PUT /products/{productId}
     */
    public ProductResponse updateProduct(UUID productId, SaveOrUpdateProductRequest requestEntity) {
        var url = getUriString(productId);
        var productResponseEntity = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(requestEntity),
                ProductResponse.class);
        return productResponseEntity.getBody();
    }

    /**
     * GET /products/{productId}
     */
    public ProductResponse getProductById(UUID productId) {
        return restTemplate.getForObject(getUriString(productId), ProductResponse.class);
    }

    private String getUriString(UUID productId) {
        return UriComponentsBuilder.fromUriString(PRODUCT_URL)
                .path("/{productId}")
                .buildAndExpand(productId)
                .toUriString();
    }
}
