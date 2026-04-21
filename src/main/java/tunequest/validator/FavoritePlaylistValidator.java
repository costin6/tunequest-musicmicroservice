package tunequest.validator;

import tunequest.entity.FavoritePlaylist;

public class FavoritePlaylistValidator {

    public static void validateFavoritePlaylist(FavoritePlaylist favoritePlaylist) {
        if (favoritePlaylist == null) {
            throw new IllegalArgumentException("FavoritePlaylist cannot be null.");
        }

        // Validate userId
        if (favoritePlaylist.getUserId() == null || favoritePlaylist.getUserId().isEmpty()) {
            throw new IllegalArgumentException("FavoritePlaylist ID cannot be null or empty.");
        }

        // Validate Playlist Link
        if (favoritePlaylist.getPlaylistLink() == null || favoritePlaylist.getPlaylistLink().isEmpty()) {
            throw new IllegalArgumentException("Playlist link cannot be null or empty.");
        }
        if (!favoritePlaylist.getPlaylistLink().startsWith("https://open.spotify.com/playlist")) {
            throw new IllegalArgumentException("Playlist link must be a valid Spotify playlist URL.");
        }
    }
}