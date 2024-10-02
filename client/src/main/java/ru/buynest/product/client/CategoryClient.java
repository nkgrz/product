package ru.buynest.product.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.buynest.product.api.request.SaveOrUpdateCategoryRequest;
import ru.buynest.product.api.response.CategoryResponse;

import java.util.UUID;

public class CategoryClient {

    private static final String CATEGORY_URL = "/categories";
    private final RestTemplate restTemplate;

    public CategoryClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * POST /categories/
     */
    public CategoryResponse createCategory(SaveOrUpdateCategoryRequest requestEntity) {
        return restTemplate.postForObject(CATEGORY_URL, requestEntity, CategoryResponse.class);
    }

    /**
     * PUT /categories/{categoryId}
     */
    public CategoryResponse updateCategory(UUID categoryId, SaveOrUpdateCategoryRequest requestEntity) {
        var url = getUriString(categoryId);
        var categoryResponseEntity = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(requestEntity),
                CategoryResponse.class);
        return categoryResponseEntity.getBody();
    }

    /**
     * GET /categories/{categoryId}
     */
    public CategoryResponse getCategoryById(UUID categoryId) {
        return restTemplate.getForObject(getUriString(categoryId), CategoryResponse.class);
    }

    private String getUriString(UUID categoryId) {
        return UriComponentsBuilder.fromUriString(CATEGORY_URL)
                .path("/{categoryId}")
                .buildAndExpand(categoryId)
                .toUriString();
    }
}