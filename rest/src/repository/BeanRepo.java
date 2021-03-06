package repository;

import model.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BeanRepo {

    static final Logger log = LoggerFactory.getLogger(BeanRepo.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Bean> getAll() {
        return jdbcTemplate.query("select NAME, VALUE from BEAN", (rs, rowNum) -> new Bean(rs.getString("NAME"), rs.getString("VALUE")));
    }
}
