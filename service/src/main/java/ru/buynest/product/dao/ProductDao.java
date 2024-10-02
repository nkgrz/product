package ru.buynest.product.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.buynest.product.model.Category;
import ru.buynest.product.model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
public class ProductDao {

    private static final String FIND_CATEGORIES_BY_PRODUCT_ID = """
            SELECT c.id AS category_id,
                       c.name AS category_name,
                       c.description AS category_description,
                       c.parent_category_id AS parent_category_id,
                       c.created_at AS created_at,
                       c.updated_at AS updated_at
                FROM category c
                JOIN product_to_category pc ON c.id = pc.category_id
            """;

    private static final String UPSERT_SQL = """
            INSERT INTO product (id, name, description, price, stock, images, specifications,
                 created_at, updated_at, is_active)
            VALUES (:id, :name, :description, :price, :stock, :images::JSONB, :specifications::jsonb,
                 :createdAt, :updatedAt, :is_active)
            ON CONFLICT (id) DO UPDATE
            SET name = :name,
                description = :description,
                price = :price,
                stock = :stock,
                images = :images::jsonb,
                specifications = :specifications::jsonb,
                updated_at = :updatedAt,
                is_active = :is_active
            """;

    private static final String FIND_PRODUCT_WITH_CATEGORIES_SQL = """
            SELECT p.id AS product_id, p.name AS product_name, p.description AS product_description, p.price, p.stock,
                   p.images, p.specifications, p.created_at AS product_created_at, p.updated_at AS product_updated_at, p.is_active,
                   c.id AS category_id, c.name AS category_name, c.description AS category_description,
                   c.parent_category_id, c.created_at AS category_created_at, c.updated_at AS category_updated_at
            FROM product p
            LEFT JOIN product_to_category pc ON p.id = pc.product_id
            LEFT JOIN category c ON pc.category_id = c.id
            """;

    private final ObjectMapper objectMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ProductDao(ObjectMapper objectMapper, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.objectMapper = objectMapper;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Product findById(UUID id) {
        return namedParameterJdbcTemplate.query(FIND_PRODUCT_WITH_CATEGORIES_SQL + " WHERE p.id = :id",
                Map.of("id", id), this::extractProduct);
    }

    @Transactional(propagation = MANDATORY)
    public int upsert(Product product) {

        Map<String, Object> params = prepareProductParams(product);

        int rowsAffected = namedParameterJdbcTemplate.update(UPSERT_SQL, params);

        updateCategoriesIfChanged(product.getCategories(), product.getId());

        return rowsAffected;
    }

    private Map<String, Object> prepareProductParams(Product product) {
        try {
            return Map.of(
                    "id", product.getId(),
                    "name", product.getName(),
                    "description", product.getDescription(),
                    "price", product.getPrice(),
                    "stock", product.getStock(),
                    "images", objectMapper.writeValueAsString(product.getImages()),
                    "specifications", objectMapper.writeValueAsString(product.getSpecifications()),
                    "createdAt", Timestamp.from(product.getCreatedAt()),
                    "updatedAt", Timestamp.from(product.getUpdatedAt()),
                    "is_active", product.isActive());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON fields", e);
        }
    }

    @Transactional
    protected void updateCategoriesIfChanged(List<Category> newCategories, UUID productId) {
        List<Category> oldCategories = findCategoriesByProductId(productId);

        if (categoriesAreDifferent(oldCategories, newCategories)) {
            deleteOldCategories(productId);
            insertNewCategories(newCategories, productId);
        }
    }

    private boolean categoriesAreDifferent(List<Category> oldCategories, List<Category> newCategories) {
        if (oldCategories.size() != newCategories.size()) {
            return true;
        }

        Set<UUID> oldCategoryIds = oldCategories.stream().map(Category::getId).collect(Collectors.toSet());
        Set<UUID> newCategoryIds = newCategories.stream().map(Category::getId).collect(Collectors.toSet());

        return !oldCategoryIds.equals(newCategoryIds);
    }

    private void deleteOldCategories(UUID productId) {
        String deleteCategoriesSql = """
                DELETE FROM product_to_category WHERE product_id = :productId
                """;

        namedParameterJdbcTemplate.update(deleteCategoriesSql, Map.of("productId", productId));
    }

    private void insertNewCategories(List<Category> categories, UUID productId) {
        if (categories == null || categories.isEmpty()) {
            return;
        }
        String insertCategorySql = """
                INSERT INTO product_to_category (product_id, category_id)
                VALUES (:productId, :categoryId)
                """;

        for (Category category : categories) {
            Map<String, Object> categoryParams = Map.of(
                    "productId", productId,
                    "categoryId", category.getId()
            );
            namedParameterJdbcTemplate.update(insertCategorySql, categoryParams);
        }
    }

    protected List<Category> findCategoriesByProductId(UUID id) {
        return namedParameterJdbcTemplate.query(
                FIND_CATEGORIES_BY_PRODUCT_ID + " WHERE pc.product_id = :productId",
                Map.of("productId", id), this::mapCategory);
    }

    private Category mapCategory(ResultSet rs, int rowNum) {
        try {
            return new Category(
                    UUID.fromString(rs.getString("category_id")),
                    rs.getString("category_name"),
                    rs.getString("category_description"),
                    rs.getObject("parent_category_id", UUID.class),
                    rs.getTimestamp("created_at").toInstant(),
                    rs.getTimestamp("updated_at").toInstant()
            );
        } catch (SQLException e) {
            throw new RuntimeException("Error processing JSON fields", e);
        }
    }

    private Product extractProduct(ResultSet rs) throws SQLException {
        Product product = null;
        List<Category> categories = new ArrayList<>();

        while (rs.next()) {
            if (product == null) {
                try {
                    product = new Product(
                            UUID.fromString(rs.getString("product_id")),
                            rs.getString("product_name"),
                            rs.getString("product_description"),
                            rs.getBigDecimal("price"),
                            rs.getInt("stock"),
                            objectMapper.readValue(rs.getString("images"), new TypeReference<>() {}),
                            objectMapper.readValue(rs.getString("specifications"), new TypeReference<>() {}),
                            rs.getTimestamp("product_created_at").toInstant(),
                            rs.getTimestamp("product_updated_at").toInstant(),
                            rs.getBoolean("is_active")
                    );
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Failed to map JSON fields", e);
                }
            }

            UUID categoryId = UUID.fromString(rs.getString("category_id"));
            if (!categoryId.toString().isEmpty()) {
                Category category = new Category(
                        categoryId,
                        rs.getString("category_name"),
                        rs.getString("category_description"),
                        rs.getObject("parent_category_id", UUID.class),
                        rs.getTimestamp("category_created_at").toInstant(),
                        rs.getTimestamp("category_updated_at").toInstant()
                );
                categories.add(category);
            }
        }

        if (product != null) {
            product.setCategories(categories);
        }
        return product;
    }
}
