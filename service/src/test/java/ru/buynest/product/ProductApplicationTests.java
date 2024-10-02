package ru.buynest.product;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import ru.buynest.product.client.ApiClientAutoConfiguration;
import ru.buynest.product.client.ApiClientProperties;
import ru.buynest.product.client.CategoryClient;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductApplicationTests {

    protected CategoryClient categoryClient;

    @LocalServerPort
    private int port;

    @PostConstruct
    public void postConstruct() {
        String baseUrl = "http://localhost:" + port;

        ApiClientProperties apiClientProperties = new ApiClientProperties();
        apiClientProperties.setBaseUrl(baseUrl);

        ApiClientAutoConfiguration apiClientAutoConfiguration = new ApiClientAutoConfiguration();

        RestTemplate restTemplate = apiClientAutoConfiguration.restTemplate(apiClientProperties);

        categoryClient = apiClientAutoConfiguration.categoryClient(restTemplate);
    }
}
