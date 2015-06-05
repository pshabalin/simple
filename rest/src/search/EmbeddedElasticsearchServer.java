package search;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PreDestroy;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@Configuration
@Profile("default")
public class EmbeddedElasticsearchServer {

    private static final Logger log = LoggerFactory.getLogger(EmbeddedElasticsearchServer.class);

    private static final String DATA_DIRECTORY = "data";

    Node node;

    EmbeddedElasticsearchServer() throws InterruptedException {
        log.info("Starting Elastic Search Server");

        ImmutableSettings.Builder elasticsearchSettings = ImmutableSettings.settingsBuilder()
                .put("http.enabled", "false")
                .put("path.data", DATA_DIRECTORY)
                .put("path.config", "config/elasticsearch")
                .put("action.auto_create_index", "false");

        node = nodeBuilder()
                .local(true)
                .settings(elasticsearchSettings.build())
                .node();

        Settings indexSettings = ImmutableSettings.settingsBuilder()
                .put("number_of_shards", 1)
                .put("number_of_replicas", 1)
                .build();
        CreateIndexRequest indexRequest = new CreateIndexRequest("twitter", indexSettings);

        CyclicBarrier barrier = new CyclicBarrier(2);
        node.client().admin().indices().create(indexRequest, new ActionListener<CreateIndexResponse>() {
            @Override
            public void onResponse(CreateIndexResponse createIndexResponse) {
                log.debug("Index created {}", createIndexResponse.isAcknowledged());
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                log.error(e.getMessage(), e);
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e1) {
                    throw new RuntimeException(e);
                }
            }
        });
        try {
            barrier.await();
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
        log.info("Done Starting");
    }

    @Bean
    Client getClient() {
        return node.client();
    }

    @PreDestroy
    void shutdown() {
        log.info("Shooting down Elastic Search Server");
        node.close();
        log.info("Done");
    }
}