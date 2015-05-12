package repository.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.stream.IntStream;

public class V1_1__InitialData implements SpringJdbcMigration {

    private static final Logger log = LoggerFactory.getLogger(V1_1__InitialData.class);

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        log.debug("Populating initial data");
        IntStream.range(0, 100).forEach(i -> jdbcTemplate.update("insert into PUBLIC.BEAN(name, value) values (?, ?)", "Key " + i, "Value " + i));
        log.debug("Done");
    }
}
