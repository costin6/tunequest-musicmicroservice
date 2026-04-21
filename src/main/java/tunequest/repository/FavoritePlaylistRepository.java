package tunequest.repository;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.PartitionKey;
import org.springframework.stereotype.Repository;
import tunequest.entity.FavoritePlaylist;

@Repository
public class FavoritePlaylistRepository {

    private final CosmosContainer cosmosContainer;

    public FavoritePlaylistRepository(CosmosContainer cosmosContainer) {
        this.cosmosContainer = cosmosContainer;
    }

    public void save(FavoritePlaylist playlist) {
        cosmosContainer.createItem(playlist);
    }

    public FavoritePlaylist findById(String id) {
        return cosmosContainer.readItem(id, new PartitionKey(id), FavoritePlaylist.class).getItem();
    }

    public void deleteById(String id) {
        CosmosItemRequestOptions options = new CosmosItemRequestOptions();
        cosmosContainer.deleteItem(id, new PartitionKey(id), options);
    }
}
