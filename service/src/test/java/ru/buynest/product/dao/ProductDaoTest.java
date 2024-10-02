package ru.buynest.product.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ru.buynest.product.ProductApplicationTests;
import ru.buynest.product.model.Category;
import ru.buynest.product.model.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDaoTest extends ProductApplicationTests {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private CategoryDao categoryDao;

    private static final UUID PRODUCT_ID = UUID.randomUUID();
    private static final String PRODUCT_NAME = "Test Product";
    private static final String PRODUCT_DESCRIPTION = "Test Description";
    private static final Instant CREATED_AT = Instant.now();
    private static final ArrayList<String> IMAGES = new ArrayList<>(List.of("image1.jpg", "image2.jpg"));
    private static final Map<String, String> SPECIFICATIONS = Map.of("color", "red", "size", "L");
    private static final String UPDATED_PRODUCT_NAME = "New product name";
    private static final String UPDATED_PRODUCT_DESCRIPTION = "New description";

    private Product product;
    private Category category1;
    private Category category2;
    private Category category3;

    @BeforeEach
    public void setUp() {
        category1 = new Category(UUID.randomUUID(), "Category 1", "Description 1", null, CREATED_AT, CREATED_AT);
        category2 = new Category(UUID.randomUUID(), "Category 2", "Description 2", null, CREATED_AT, CREATED_AT);
        category3 = new Category(UUID.randomUUID(), "Category 3", "Description 2", null, CREATED_AT, CREATED_AT);

        categoryDao.upsert(category1);
        categoryDao.upsert(category2);
        categoryDao.upsert(category3);

        product = new Product(
                PRODUCT_ID,
                PRODUCT_NAME,
                PRODUCT_DESCRIPTION,
                new BigDecimal("99.99"),
                10,
                IMAGES,
                SPECIFICATIONS,
                CREATED_AT,
                CREATED_AT,
                true
        );

        ArrayList<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);
        product.setCategories(categories);
    }

    @Test
    @Transactional
    public void shouldInsertProductAndFindByIdSuccessfully() {
        int rowsInserted = productDao.upsert(product);
        assertEquals(1, rowsInserted);

        Product foundProduct = productDao.findById(PRODUCT_ID);

        assertAll("Product properties",
                () -> assertEquals(PRODUCT_ID, foundProduct.getId()),
                () -> assertEquals(PRODUCT_NAME, foundProduct.getName()),
                () -> assertEquals(IMAGES, foundProduct.getImages()),
                () -> assertEquals(SPECIFICATIONS, foundProduct.getSpecifications())
        );

        List<Category> foundCategories = foundProduct.getCategories();
        assertAll("Product categories",
                () -> assertEquals(2, foundCategories.size()),
                () -> assertEquals(category1.getId(), foundCategories.getFirst().getId()),
                () -> assertEquals(category2.getId(), foundCategories.getLast().getId())
        );
    }

    @Test
    @Transactional
    public void shouldUpdateProductAndCategoriesSuccessfully() {

        productDao.upsert(product);

        Instant updatedAt = Instant.now();

        product.setName(UPDATED_PRODUCT_NAME);
        product.setDescription(UPDATED_PRODUCT_DESCRIPTION);
        product.setCategories(List.of(category3));
        product.setUpdatedAt(updatedAt);

        int rowsUpdated = productDao.upsert(product);
        assertEquals(1, rowsUpdated);

        Product updatedProduct = productDao.findById(PRODUCT_ID);

        assertAll("Updated product properties",
                () -> assertEquals(UPDATED_PRODUCT_NAME, updatedProduct.getName()),
                () -> assertEquals(UPDATED_PRODUCT_DESCRIPTION, updatedProduct.getDescription()),
                () -> assertEquals(1, updatedProduct.getCategories().size()),
                () -> assertEquals(category3.getId(), updatedProduct.getCategories().getFirst().getId()),
                () -> assertEquals(updatedAt, updatedProduct.getUpdatedAt())
        );

    }
}