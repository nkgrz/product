package ru.buynest.product.helper.dao;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.buynest.product.dao.CategoryDao;
import ru.buynest.product.model.Category;

@Repository
public class TestCategoryDao extends CategoryDao {

    public TestCategoryDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(namedParameterJdbcTemplate);
    }

    @Override
    @Transactional
    public int upsert(Category category) {
        return super.upsert(category);
    }
}
