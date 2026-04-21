package tunequest.business;

import org.springframework.stereotype.Service;
import tunequest.entity.FavoritePlaylist;
import tunequest.repository.FavoritePlaylistRepository;
import tunequest.validator.FavoritePlaylistValidator;

@Service
public class FavoritePlaylistService {

    private final FavoritePlaylistRepository favoritePlaylistRepository;

    public FavoritePlaylistService(FavoritePlaylistRepository favoritePlaylistRepository) {
        this.favoritePlaylistRepository = favoritePlaylistRepository;
    }

    public FavoritePlaylist getUserFavoritePlaylist(String id) {
        try {
            return favoritePlaylistRepository.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public void saveDefaultPlaylist(String id) {
        FavoritePlaylist newPlaylist = new FavoritePlaylist(
                id,
                "https://open.spotify.com/playlist/26UqdlEO5Rra0SJMBgoVAm?si=dd99f867129c4aaa"
        );
        FavoritePlaylistValidator.validateFavoritePlaylist(newPlaylist);
        favoritePlaylistRepository.save(newPlaylist);
    }

    public void deleteUserData(String id) {
        favoritePlaylistRepository.deleteById(id);
    }
}