package repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import model.Node;
import model.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public class NodeRepo {

    static final Logger log = LoggerFactory.getLogger(NodeRepo.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public UUID save(Node node) {
        try {
            node.id = UUID.randomUUID();
            node.created = new Date();

            ObjectWriter writer = objectMapper.writer();
            String json = writer.writeValueAsString(node);
            log.debug(json);

            jdbcTemplate.update("INSERT INTO node(id, type, name, parent, description, created, payload) VALUES (?, ?, ?, ?, ?)",
                    node.id, node.getClass().getName(), node.name, node.parent.id, node.description, node.created, json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return node.id;
    }

    public void update(Node node) {
        ObjectWriter writer = objectMapper.writer();
        try {
            node.updated = new Date();
            String json = writer.writeValueAsString(node);
            log.debug(json);

            jdbcTemplate.update("UPDATE node SET name = ?, parent = ?, description = ?, payload = ?, updated = ?) WHERE id = ?",
                    node.name, node.parent.id, node.description, json, node.updated, node.id);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(UUID id) {
        jdbcTemplate.update("DELETE node WHERE id = ?", id);
    }

    public Node load(UUID id) {
        return jdbcTemplate.queryForObject("SELECT type, payload FROM node WHERE id = ?", new Object[]{id}, new NodeRowMapper());
    }

    public List<Node> loadChildren(UUID id) {
        return jdbcTemplate.query("SELECT type, payload FROM node WHERE parent = ?", new Object[]{id}, new NodeRowMapper());
    }


    public List<Node> loadRootChildren() {
        UUID rootId = jdbcTemplate.queryForObject("SELECT id FROM node WHERE type=?",
                new Object[]{Root.class.getName()}, UUID.class);
        return loadChildren(rootId);
    }

    public Node loadRoot() {
        return jdbcTemplate.queryForObject("SELECT type, payload FROM node WHERE type = ?", new Object[]{Root.class.getName()}, new NodeRowMapper());
    }

    private class NodeRowMapper implements RowMapper<Node> {
        @Override
        public Node mapRow(ResultSet rs, int rowNum) throws SQLException {
            try {
                String className = rs.getString("type");
                Class<? extends Node> nodeClass = (Class<? extends Node>) Class.forName(className);
                log.debug("Node class name: {}", className);

                String json = rs.getString("payload");
                log.debug(json);

                return objectMapper.readValue(json, nodeClass);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
