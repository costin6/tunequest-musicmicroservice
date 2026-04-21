package tunequest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tunequest.business.SpotifyService;

import java.util.Map;

@RestController
@RequestMapping("/api/spotify/search")
public class SpotifyController {

    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/playlists")
    public ResponseEntity<Map<String, Object>> searchPlaylists(
            @RequestParam(name = "search") String search,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            // Validating the search query
            if (!search.matches("[a-zA-Z0-9 _-]{1,100}")) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid search query. Allowed characters are letters, numbers, spaces, and basic symbols."));
            }

            String accessToken = authHeader.replace("Bearer ", "").trim();

            Map<String, Object> playlists = spotifyService.searchPlaylists(accessToken, search);

            return ResponseEntity.ok(playlists);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred."));
        }
    }


    @GetMapping("/playlists/tracks")
    public ResponseEntity<Map<String, Object>> getPlaylistTracks(
            @RequestParam(name = "playlistId") String playlistId,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String accessToken = authHeader.replace("Bearer ", "").trim();

            Map<String, Object> tracks = spotifyService.getPlaylistTracks(accessToken, playlistId);

            return ResponseEntity.ok(tracks);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}