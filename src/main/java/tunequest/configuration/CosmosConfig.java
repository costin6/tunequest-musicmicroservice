package tunequest.configuration;

import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.CosmosClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CosmosConfig {

    private static final String ENDPOINT = "https://tunequestmusicdb.documents.azure.com:443/";
    private static final String KEY = "SECRET";
    private static final String DATABASE_NAME = "tunequestmusicdb";
    private static final String CONTAINER_NAME = "favorite_playlist";

    @Bean
    public CosmosClient cosmosClient() {
        return new CosmosClientBuilder()
                .endpoint(ENDPOINT)
                .key(KEY)
                .consistencyLevel(com.azure.cosmos.ConsistencyLevel.SESSION)
                .buildClient();
    }

    @Bean
    public CosmosDatabase cosmosDatabase(CosmosClient cosmosClient) {
        return cosmosClient.getDatabase(DATABASE_NAME);
    }

    @Bean
    public CosmosContainer favoritePlaylistContainer(CosmosDatabase cosmosDatabase) {
        return cosmosDatabase.getContainer(CONTAINER_NAME);
    }
}
