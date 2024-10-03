package ru.buynest.product.api.request;

import ru.buynest.product.api.response.CategoryResponse;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class SaveOrUpdateProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private List<CategoryResponse> categories;
    private Integer stock;
    private List<String> images;
    private Map<String, String> specifications;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean isActive;

    public SaveOrUpdateProductRequest(String name, String description, BigDecimal price,
                                      List<CategoryResponse> categories, Integer stock,
                                      List<String> images, Map<String, String> specifications,
                                      Instant createdAt, Instant updatedAt, boolean isActive) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.categories = categories;
        this.stock = stock;
        this.images = images;
        this.specifications = specifications;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<CategoryResponse> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryResponse> categories) {
        this.categories = categories;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Map<String, String> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Map<String, String> specifications) {
        this.specifications = specifications;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
