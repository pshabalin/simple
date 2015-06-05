package repository.migration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import config.RepositoryConfig;
import model.Root;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.UUID;

public class V1_3__RepositoryRoot implements SpringJdbcMigration {

    private static final Logger log = LoggerFactory.getLogger(V1_3__RepositoryRoot.class);

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        //INSERT INTO node(id, type, name, parent, description, created, payload) VALUES (RANDOM_UUID(), '', null, '', CURRENT_TIMESTAMP(), 'model.Root', '{}');

        try {
            Root node = new Root();
            node.id = UUID.randomUUID();
            node.created = new Date();

            node.name = "";
            node.description = "";

            ObjectWriter writer = RepositoryConfig.objectMapper().writer();
            String json = writer.writeValueAsString(node);
            log.debug(json);

            jdbcTemplate.update("INSERT INTO node(id, type, name, parent, description, created, payload) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    node.id, node.getClass().getName(), node.name, null, node.description, node.created, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
