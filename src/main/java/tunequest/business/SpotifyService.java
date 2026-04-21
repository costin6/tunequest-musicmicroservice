package tunequest.business;

import java.util.Map;

public interface SpotifyService {
    Map<String, Object> searchPlaylists(String accessToken, String query);
    Map<String, Object> getPlaylistTracks(String accessToken, String playlistId);

}
