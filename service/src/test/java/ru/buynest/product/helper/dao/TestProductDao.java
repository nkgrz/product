package ru.buynest.product.helper.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.buynest.product.dao.ProductDao;
import ru.buynest.product.model.Product;

import java.util.HashMap;

@Repository
public class TestProductDao extends ProductDao {

    NamedParameterJdbcTemplate jdbcTemplate;

    public TestProductDao(ObjectMapper objectMapper, NamedParameterJdbcTemplate jdbcTemplate) {
        super(objectMapper, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public int upsert(Product product) {
        return super.upsert(product);
    }

    @Transactional
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM product_to_category", new HashMap<>());
        jdbcTemplate.update("DELETE FROM product", new HashMap<>());
    }
}
