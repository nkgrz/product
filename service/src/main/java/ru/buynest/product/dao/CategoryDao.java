package ru.buynest.product.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.buynest.product.model.Category;

import java.sql.Timestamp;
import java.util.*;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
public class CategoryDao {

    // language=sql
    private final String FIND_BY = """
            SELECT c.id AS category_id,
                       c.name AS category_name,
                       c.description AS category_description,
                       c.parent_category_id AS parent_category_id,
                       c.created_at AS created_at,
                       c.updated_at AS updated_at
                FROM category c
            """;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CategoryDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Optional<Category> findById(UUID id) {
        // language=sql
        String sql = FIND_BY + " WHERE c.id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        List<Category> categories = getCategories(sql, params);

        return categories.isEmpty() ? Optional.empty() : Optional.of(categories.getFirst());
    }

    public List<Category> findByNameAndParentCategoryId(String name, UUID parentCategoryId) {
        // language=sql
        String sql = FIND_BY + " WHERE c.name = :name " +
                (parentCategoryId == null ?
                        "AND c.parent_category_id IS NULL"
                        : "AND c.parent_category_id = :parentCategoryId");
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("parentCategoryId", parentCategoryId);

        return getCategories(sql, params);
    }

    @Transactional(propagation = MANDATORY)
    public int upsert(Category category) {
        String sql = """
                INSERT INTO category (id, name, description, parent_category_id, created_at, updated_at)
                VALUES (:id, :name, :description, :parentCategoryId, :createdAt, :updatedAt)
                ON CONFLICT (id) DO UPDATE
                SET name = :name,
                    description = :description,
                    parent_category_id = :parentCategoryId,
                    updated_at = :updatedAt
                """;

        Map<String, Object> params = new HashMap<>();
        params.put("id", category.getId());
        params.put("name", category.getName());
        params.put("description", category.getDescription());
        params.put("parentCategoryId", category.getParentCategoryId());
        params.put("createdAt", Timestamp.from(category.getCreatedAt()));
        params.put("updatedAt", Timestamp.from(category.getUpdatedAt()));

        return namedParameterJdbcTemplate.update(sql, params);
    }

    private List<Category> getCategories(String sql, Map<String, Object> params) {
        return namedParameterJdbcTemplate.query(sql, params, (rs, _) -> new Category(
                UUID.fromString(rs.getString("category_id")),
                rs.getString("category_name"),
                rs.getString("category_description"),
                rs.getObject("parent_category_id", UUID.class),
                rs.getTimestamp("created_at").toInstant(),
                rs.getTimestamp("updated_at").toInstant()
        ));
    }
}
