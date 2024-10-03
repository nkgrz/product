package ru.buynest.product.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import ru.buynest.product.ProductApplicationTests;
import ru.buynest.product.api.request.SaveOrUpdateCategoryRequest;
import ru.buynest.product.api.response.CategoryResponse;
import ru.buynest.product.helper.dao.TestCategoryDao;
import ru.buynest.product.model.Category;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryClientTest extends ProductApplicationTests {

    private final String CATEGORY_NAME = "Electronics";
    private final String CATEGORY_DESCRIPTION = "Electronic items";
    private final Instant CATEGORY_CREATED_AT = Instant.now();
    private final String UPDATED_CATEGORY_NAME = "Updated Electronics";
    private final String UPDATED_CATEGORY_DESCRIPTION = "Electronic items";
    private final UUID CATEGORY_ID = UUID.randomUUID();

    private final SaveOrUpdateCategoryRequest CREATE_CATEGORY_REQUEST = new SaveOrUpdateCategoryRequest(
            CATEGORY_NAME, CATEGORY_DESCRIPTION, null,
            CATEGORY_CREATED_AT, CATEGORY_CREATED_AT);
    private final Category CATEGORY = new Category(
            CATEGORY_ID, CATEGORY_NAME, CATEGORY_DESCRIPTION,
            null, CATEGORY_CREATED_AT, CATEGORY_CREATED_AT
    );

    @Autowired
    private TestCategoryDao categoryDao;

    @BeforeEach
    void before() {
        categoryDao.deleteAll();
    }

    @Test
    @DisplayName("Should successfully create a category and verify its properties")
    public void shouldCreateCategorySuccessfully() {
        CategoryResponse categoryResponse = categoryClient.createCategory(CREATE_CATEGORY_REQUEST);
        assertCategoryProperties(categoryResponse, CATEGORY_NAME, CATEGORY_DESCRIPTION, CATEGORY_CREATED_AT);
    }

    @Test
    @DisplayName("Should update a category successfully and verify updated properties")
    public void shouldUpdateCategorySuccessfully() {
        categoryDao.upsert(CATEGORY);
        Instant updateTime = Instant.now();
        SaveOrUpdateCategoryRequest updateCategoryRequest = new SaveOrUpdateCategoryRequest(
                UPDATED_CATEGORY_NAME, UPDATED_CATEGORY_DESCRIPTION, null,
                CATEGORY_CREATED_AT, updateTime);

        CategoryResponse categoryResponse = categoryClient.updateCategory(CATEGORY_ID, updateCategoryRequest);

        assertAll("Category properties",
                () -> assertEquals(UPDATED_CATEGORY_NAME, categoryResponse.getName()),
                () -> assertEquals(UPDATED_CATEGORY_DESCRIPTION, categoryResponse.getDescription()),
                () -> assertEquals(updateTime, categoryResponse.getUpdatedAt())
        );
    }

    @Test
    @DisplayName("Should retrieve category by ID and verify its properties")
    public void shouldGetCategorySuccessfully() {
        categoryDao.upsert(CATEGORY);
        CategoryResponse categoryResponse = categoryClient.getCategoryById(CATEGORY_ID);
        assertCategoryProperties(categoryResponse, CATEGORY_NAME, CATEGORY_DESCRIPTION, CATEGORY_CREATED_AT);
    }

    @Test
    @DisplayName("Should return 404 Not Found for non-existent category ID")
    public void shouldReturnNotFoundForNonExistentCategory() {
        UUID randomCategoryId = UUID.randomUUID();

        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.NotFound.class,
                () -> categoryClient.getCategoryById(randomCategoryId),
                "Expected getCategoryById to throw, but it didn't"
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode(), "Expected 404 Not Found error");
    }

    @Test
    @DisplayName("Should return 409 Conflict when creating an existing category")
    public void shouldReturnCategoryExistExceptionWhenCategoryExist() {
        categoryDao.upsert(CATEGORY);
        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.Conflict.class,
                () -> categoryClient.createCategory(CREATE_CATEGORY_REQUEST),
                "Expected create category to throw, but it didn't"
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode(), "Expected conflict error");
    }

    private void assertCategoryProperties(CategoryResponse categoryResponse, String name, String description, Instant createdAt) {
        assertAll("Category properties",
                () -> assertEquals(name, categoryResponse.getName()),
                () -> assertEquals(description, categoryResponse.getDescription()),
                () -> assertEquals(createdAt, categoryResponse.getCreatedAt())
        );
    }
}