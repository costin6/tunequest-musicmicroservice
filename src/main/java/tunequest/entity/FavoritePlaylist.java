package tunequest.entity;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

public class FavoritePlaylist {

    @JsonProperty("id")
    private String id;

    private String playlistLink;

    // Getters and setters
    public FavoritePlaylist() {}

    public FavoritePlaylist(String id, String playlistLink) {
        this.id = id;
        this.playlistLink = playlistLink;
    }

    public String getUserId() {
        return id;
    }

    public void setUserId(String userId) {
        this.id = userId;
    }

    public String getPlaylistLink() {
        return playlistLink;
    }

    public void setPlaylistLink(String playlistLink) {
        this.playlistLink = playlistLink;
    }
}
