package ru.buynest.product.api.request;

import java.time.Instant;
import java.util.UUID;

public class SaveOrUpdateCategoryRequest {
    private String name;
    private String description;
    private UUID parentCategoryId;
    private Instant createdAt;
    private Instant updatedAt;

    public SaveOrUpdateCategoryRequest(String name, String description, UUID parentCategoryId, Instant createdAt, Instant updatedAt) {
        this.name = name;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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