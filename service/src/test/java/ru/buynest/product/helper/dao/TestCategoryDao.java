package ru.buynest.product.helper.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.buynest.product.dao.CategoryDao;
import ru.buynest.product.model.Category;

import java.util.HashMap;

@Repository
public class TestCategoryDao extends CategoryDao {

    NamedParameterJdbcTemplate jdbcTemplate;

    public TestCategoryDao(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public int upsert(Category category) {
        return super.upsert(category);
    }

    @Transactional
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM category", new HashMap<>());
    }
}
