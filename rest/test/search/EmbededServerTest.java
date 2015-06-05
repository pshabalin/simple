package search;


import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

@Test(enabled = true)
public class EmbededServerTest {

    private static final Logger log = LoggerFactory.getLogger(EmbededServerTest.class);

    public void testStart() throws InterruptedException {
         EmbeddedElasticsearchServer server = new EmbeddedElasticsearchServer();
         Client client = server.getClient();

         String json = "{" +
                 "\"user\":\"kimchy\"," +
                 "\"postDate\":\"2013-01-30\"," +
                 "\"message\":\"trying out Elasticsearch\"" +
                 "}";

         IndexResponse response = client.prepareIndex("twitter", "tweet")
                 .setSource(json)
                 .execute()
                 .actionGet();

         // Index name
         String _index = response.getIndex();
        // Type name
         String _type = response.getType();
         // Document ID (generated or not)
         String _id = response.getId();
         // Version (if it's the first time you index this document, you will get: 1)
         long _version = response.getVersion();
         // isCreated() is true if the document is a new one, false if it has been updated
         boolean created = response.isCreated();

        log.debug("Index: {}, type: {}, id: {}, version: {}, created: {}", _index, _type, _id, _version, created);

        server.shutdown();
     }

}
