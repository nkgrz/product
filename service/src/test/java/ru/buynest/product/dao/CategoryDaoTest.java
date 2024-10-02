package ru.buynest.product.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.buynest.product.ProductApplicationTests;
import ru.buynest.product.model.Category;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryDaoTest extends ProductApplicationTests {

    private static final UUID CATEGORY_ID = UUID.randomUUID();
    private static final String CATEGORY_NAME = "Electronics";
    private static final String CATEGORY_DESCRIPTION = "Electronic items";
    private static final Instant CATEGORY_CREATED_AT = Instant.now();
    private static final String UPDATED_CATEGORY_NAME = "Updated Electronics";
    private static final String UPDATED_CATEGORY_DESCRIPTION = "Electronic items";

    @Autowired
    private CategoryDao categoryDao;
    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category(
                CATEGORY_ID,
                CATEGORY_NAME,
                CATEGORY_DESCRIPTION,
                null,
                CATEGORY_CREATED_AT,
                CATEGORY_CREATED_AT
        );
    }

    @Test
    @Transactional
    public void shouldInsertCategoryAndFindByIdSuccessfully() {
        int rowsInserted = categoryDao.upsert(category);
        assertEquals(1, rowsInserted);

        Category foundCategory = categoryDao.findById(CATEGORY_ID).get();

        assertAll("Category properties",
                () -> assertEquals(CATEGORY_NAME, foundCategory.getName()),
                () -> assertEquals(CATEGORY_DESCRIPTION, foundCategory.getDescription()),
                () -> assertEquals(CATEGORY_CREATED_AT, foundCategory.getCreatedAt())

        );
    }

    @Test
    @Transactional
    public void shouldUpdateCategoriesSuccessfully() {
        categoryDao.upsert(category);

        Instant updatedAt = Instant.now();

        category.setName(UPDATED_CATEGORY_NAME);
        category.setDescription(UPDATED_CATEGORY_DESCRIPTION);
        category.setUpdatedAt(updatedAt);

        int rowsUpdated = categoryDao.upsert(category);
        assertEquals(1, rowsUpdated);

        Category updatedCategory = categoryDao.findById(CATEGORY_ID).get();
        assertEquals(UPDATED_CATEGORY_NAME, updatedCategory.getName());
        assertEquals(UPDATED_CATEGORY_DESCRIPTION, updatedCategory.getDescription());
        assertEquals(updatedAt, updatedCategory.getUpdatedAt());
    }

}
