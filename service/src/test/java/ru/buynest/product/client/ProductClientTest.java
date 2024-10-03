package ru.buynest.product.client;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import ru.buynest.product.ProductApplicationTests;
import ru.buynest.product.api.request.SaveOrUpdateProductRequest;
import ru.buynest.product.api.response.CategoryResponse;
import ru.buynest.product.api.response.ProductResponse;
import ru.buynest.product.helper.dao.TestCategoryDao;
import ru.buynest.product.helper.dao.TestProductDao;
import ru.buynest.product.model.Category;
import ru.buynest.product.model.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static ru.buynest.product.service.converter.CategoryConverter.getCategoryResponseListFromCategoryList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductClientTest extends ProductApplicationTests {

    private final UUID PRODUCT_ID = UUID.randomUUID();
    private final String PRODUCT_NAME = "Test Product";
    private final String PRODUCT_DESCRIPTION = "Test Description";
    private final BigDecimal PRICE = new BigDecimal("100.00");
    private final Instant CREATED_AT = Instant.now();
    private final ArrayList<String> IMAGES = new ArrayList<>(List.of("image1.jpg", "image2.jpg"));
    private final Map<String, String> SPECIFICATIONS = Map.of("color", "red", "size", "L");
    private final Category CATEGORY1 = createCategory("Category 1", "Description 1");
    private final Category CATEGORY2 = createCategory("Category 2", "Description 2");
    private final Product PRODUCT = new Product(
            PRODUCT_ID, PRODUCT_NAME, PRODUCT_DESCRIPTION, PRICE, 10,
            IMAGES, SPECIFICATIONS, CREATED_AT, CREATED_AT, true
    );

    @Autowired
    private TestCategoryDao categoryDao;
    @Autowired
    private TestProductDao productDao;

    @BeforeAll
    public void setUp() {
        categoryDao.upsert(CATEGORY1);
        categoryDao.upsert(CATEGORY2);

        List<Category> categories = List.of(CATEGORY1, CATEGORY2);
        PRODUCT.setCategories(categories);
    }

    @BeforeEach
    public void before() {
        productDao.deleteAll();
    }

    @Test
    @DisplayName("Should successfully create a product and verify its properties")
    public void shouldCreateProductSuccessfully() {
        List<CategoryResponse> categoriesResponse = getCategoryResponseListFromCategoryList(
                List.of(CATEGORY1, CATEGORY2));
        SaveOrUpdateProductRequest productRequest = createProductRequest(
                PRODUCT_NAME, PRODUCT_DESCRIPTION, categoriesResponse, CREATED_AT);

        ProductResponse productResponse = productClient.createProduct(productRequest);
        assertProductProperties(productResponse, PRODUCT_NAME, PRODUCT_DESCRIPTION,
                categoriesResponse, CREATED_AT);
    }

    @Test
    @DisplayName("Should update a product successfully and verify updated properties")
    public void shouldUpdateProductSuccessfully() {
        productDao.upsert(PRODUCT);

        String newProductName = "New product name";
        String newProductDescription = "New description";
        Instant updateTime = Instant.now();

        List<CategoryResponse> categoriesResponse = getCategoryResponseListFromCategoryList(
                List.of(CATEGORY1));
        SaveOrUpdateProductRequest productRequest = createProductRequest(
                newProductName, newProductDescription, categoriesResponse, updateTime);

        ProductResponse productResponse = productClient.updateProduct(PRODUCT_ID, productRequest);
        assertProductProperties(productResponse, newProductName, newProductDescription,
                categoriesResponse, updateTime);
    }


    @Test
    @DisplayName("Should retrieve product by ID and verify its properties")
    public void shouldGetProductSuccessfully() {
        productDao.upsert(PRODUCT);

        ProductResponse productResponse = productClient.getProductById(PRODUCT_ID);
        List<CategoryResponse> categoriesResponse = getCategoryResponseListFromCategoryList(List.of(CATEGORY1, CATEGORY2));

        assertProductProperties(productResponse, PRODUCT_NAME, PRODUCT_DESCRIPTION,
                categoriesResponse, CREATED_AT);
    }

    @Test
    @DisplayName("Should return 404 Not Found for non-existent product ID")
    public void shouldReturnNotFoundForNonExistentProduct() {
        UUID randomProductId = UUID.randomUUID();

        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.NotFound.class,
                () -> productClient.getProductById(randomProductId),
                "Expected getProductById to throw, but it didn't"
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode(), "Expected 404 Not Found error");
    }

    private void assertProductProperties(ProductResponse productResponse, String name, String description,
                                         List<CategoryResponse> categories, Instant updatedAt) {
        assertAll("Product properties validation failed",
                () -> assertEquals(name, productResponse.getName()),
                () -> assertEquals(description, productResponse.getDescription()),
                () -> assertEquals(categories, productResponse.getCategories()),
                () -> assertEquals(updatedAt, productResponse.getUpdatedAt())
        );
    }

    private Category createCategory(String name, String description) {
        return new Category(UUID.randomUUID(), name, description, null, CREATED_AT, CREATED_AT);
    }

    private SaveOrUpdateProductRequest createProductRequest(String name, String description,
                                                            List<CategoryResponse> categories, Instant updateTime) {
        return new SaveOrUpdateProductRequest(name, description, PRICE, categories, 10,
                IMAGES, SPECIFICATIONS, CREATED_AT, updateTime, true);
    }
}
