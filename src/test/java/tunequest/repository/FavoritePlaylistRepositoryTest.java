package tunequest.repository;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.models.PartitionKey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tunequest.entity.FavoritePlaylist;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FavoritePlaylistRepositoryTest {

    @Autowired
    private FavoritePlaylistRepository repository;

    @Test
    void testSaveAndFind() {
        String userId = "12345";
        FavoritePlaylist playlist = new FavoritePlaylist(userId, "https://example.com/playlist");

        repository.save(playlist);
        FavoritePlaylist found = repository.findById(userId);

        assertNotNull(found);
        assertEquals(userId, found.getUserId());
    }

    @Test
    void testDelete() {
        String userId = "12345";
        FavoritePlaylist playlist = new FavoritePlaylist(userId, "https://example.com/playlist");

        repository.save(playlist);
        repository.deleteById(userId);

        assertNull(repository.findById(userId));
    }
}
