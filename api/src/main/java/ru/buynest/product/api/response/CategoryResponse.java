package ru.buynest.product.api.response;

import java.time.Instant;
import java.util.UUID;

public class CategoryResponse {
    private UUID id;
    private String name;
    private String description;
    private UUID parentCategoryId;
    private Instant createdAt;
    private Instant updatedAt;

    public CategoryResponse(UUID id, String name, String description, UUID parentCategoryId, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public CategoryResponse() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public UUID getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(UUID parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
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
}
