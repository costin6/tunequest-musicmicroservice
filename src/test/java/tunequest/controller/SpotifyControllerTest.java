package tunequest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tunequest.business.SpotifyService;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SpotifyController.class)
class SpotifyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpotifyService spotifyService;

    @Test
    void searchPlaylists_ShouldReturnPlaylists_WhenSuccessful() throws Exception {
        // Mock data
        Map<String, Object> mockResponse = Map.of(
                "playlists", Map.of(
                        "items", List.of(
                                Map.of("name", "Chill Vibes", "id", "1"),
                                Map.of("name", "Workout Mix", "id", "2")
                        )
                )
        );

        // Mock behavior
        when(spotifyService.searchPlaylists(eq("mock-access-token"), eq("chill")))
                .thenReturn(mockResponse);

        // Perform GET request
        mockMvc.perform(get("/api/spotify/search/playlists")
                        .param("search", "chill")
                        .header("Authorization", "Bearer mock-access-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.playlists.items[0].name").value("Chill Vibes"))
                .andExpect(jsonPath("$.playlists.items[1].name").value("Workout Mix"));
    }

    @Test
    void getPlaylistTracks_ShouldReturnTracks_WhenSuccessful() throws Exception {
        // Mock data
        Map<String, Object> mockResponse = Map.of(
                "tracks", Map.of(
                        "items", List.of(
                                Map.of("name", "Song A", "id", "101"),
                                Map.of("name", "Song B", "id", "102")
                        )
                )
        );

        // Mock behavior
        when(spotifyService.getPlaylistTracks(eq("mock-access-token"), eq("playlist-1")))
                .thenReturn(mockResponse);

        // Perform GET request
        mockMvc.perform(get("/api/spotify/search/playlists/tracks")
                        .param("playlistId", "playlist-1")
                        .header("Authorization", "Bearer mock-access-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tracks.items[0].name").value("Song A"))
                .andExpect(jsonPath("$.tracks.items[1].name").value("Song B"));
    }

    @Test
    void searchPlaylists_ShouldReturnError_WhenExceptionOccurs() throws Exception {
        // Mock behavior
        when(spotifyService.searchPlaylists(any(), any()))
                .thenThrow(new RuntimeException("Something went wrong"));

        // Perform GET request
        mockMvc.perform(get("/api/spotify/search/playlists")
                        .param("search", "chill")
                        .header("Authorization", "Bearer mock-access-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Something went wrong"));
    }

    @Test
    void getPlaylistTracks_ShouldReturnError_WhenExceptionOccurs() throws Exception {
        // Mock behavior
        when(spotifyService.getPlaylistTracks(any(), any()))
                .thenThrow(new RuntimeException("Something went wrong"));

        // Perform GET request
        mockMvc.perform(get("/api/spotify/search/playlists/tracks")
                        .param("playlistId", "playlist-1")
                        .header("Authorization", "Bearer mock-access-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Something went wrong"));
    }
}
