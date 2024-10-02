package ru.buynest.product.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public class Category {
    private final UUID id;
    private String name;
    private String description;
    private UUID parentCategoryId;
    private final Instant createdAt;
    private Instant updatedAt;

    public Category(String name, String description, UUID parentCategoryId) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    public Category(UUID id, String name, String description, UUID parentCategoryId, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public UUID getParentCategoryId() {
        return parentCategoryId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParentCategoryId(UUID parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}