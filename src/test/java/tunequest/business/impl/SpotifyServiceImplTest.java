package tunequest.business.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tunequest.business.SpotifyService;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpotifyServiceImplTest {

    private SpotifyServiceImpl spotifyService;
    private HttpURLConnection mockConnection;

    @BeforeEach
    void setUp() {
        spotifyService = new SpotifyServiceImpl();
        mockConnection = mock(HttpURLConnection.class);
    }

    @Test
    void testSearchPlaylists_Success() throws Exception {
        // Arrange
        String accessToken = "validAccessToken";
        String query = "chill";
        String expectedResponse = "{\"playlists\": {\"items\": [ {\"name\": \"Chill Vibes\"} ] } }";

        URL mockUrl = mock(URL.class);
        InputStream mockInputStream = new ByteArrayInputStream(expectedResponse.getBytes());
        when(mockUrl.openConnection()).thenReturn(mockConnection);
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(mockConnection.getInputStream()).thenReturn(mockInputStream);

        // Replace the URL creation with a mock
        SpotifyServiceImpl serviceSpy = Mockito.spy(spotifyService);
        doReturn(mockUrl).when(serviceSpy).createUrl(anyString());

        // Act
        Map<String, Object> result = serviceSpy.searchPlaylists(accessToken, query);

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("playlists"));
        Map<String, Object> playlistsMap = (Map<String, Object>) result.get("playlists");
        List<Map<String, Object>> items = (List<Map<String, Object>>) playlistsMap.get("items");
        String playlistName = (String) items.get(0).get("name");
        assertEquals("Chill Vibes", playlistName);

        verify(mockConnection).setRequestMethod("GET");
        verify(mockConnection).setRequestProperty("Authorization", "Bearer " + accessToken);
        verify(mockConnection).setRequestProperty("Content-Type", "application/json");
    }

    @Test
    void testSearchPlaylists_Failure_AuthenticationError() throws Exception {
        // Arrange
        String accessToken = "invalidAccessToken";
        String query = "chill";
        String expectedErrorResponse = "{\"error\": {\"status\": 401, \"message\": \"Invalid access token\"}}";

        URL mockUrl = mock(URL.class);
        InputStream mockErrorStream = new ByteArrayInputStream(expectedErrorResponse.getBytes());
        when(mockUrl.openConnection()).thenReturn(mockConnection);
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_UNAUTHORIZED);
        when(mockConnection.getErrorStream()).thenReturn(mockErrorStream);

        // Replace the URL creation with a mock
        SpotifyServiceImpl serviceSpy = Mockito.spy(spotifyService);
        doReturn(mockUrl).when(serviceSpy).createUrl(anyString());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serviceSpy.searchPlaylists(accessToken, query);
        });
        System.out.println("Exception message: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("Spotify API Error: 401"));
    }

    @Test
    void testSearchPlaylists_Failure_BadRequest() throws Exception {
        // Arrange
        String accessToken = "validAccessToken";
        String query = "chill";
        String expectedErrorResponse = "{\"error\": {\"status\": 400, \"message\": \"Bad Request\"}}";

        URL mockUrl = mock(URL.class);
        InputStream mockErrorStream = new ByteArrayInputStream(expectedErrorResponse.getBytes());
        when(mockUrl.openConnection()).thenReturn(mockConnection);
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
        when(mockConnection.getErrorStream()).thenReturn(mockErrorStream);

        // Replace the URL creation with a mock
        SpotifyServiceImpl serviceSpy = Mockito.spy(spotifyService);
        doReturn(mockUrl).when(serviceSpy).createUrl(anyString());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serviceSpy.searchPlaylists(accessToken, query);
        });
        assertTrue(exception.getMessage().contains("Spotify API Error: 400"));
    }

    @Test
    void testSearchPlaylists_Failure_Exception() throws Exception {
        // Arrange
        String accessToken = "validAccessToken";
        String query = "chill";

        // Create the mock URL
        URL mockUrl = mock(URL.class);
        // Create the mock connection
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);

        // Mock the openConnection to throw an exception
        when(mockUrl.openConnection()).thenThrow(new RuntimeException("Connection error"));

        // Replace the URL creation with a mock
        SpotifyServiceImpl serviceSpy = Mockito.spy(spotifyService);
        doReturn(mockUrl).when(serviceSpy).createUrl(anyString());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serviceSpy.searchPlaylists(accessToken, query);
        });

        // Ensure the exception message contains the expected details
        assertTrue(exception.getMessage().contains("Connection error"));
    }

    @Test
    void testGetPlaylistTracks_Success() throws Exception {
        // Arrange
        String accessToken = "validAccessToken";
        String playlistId = "playlist123";
        String expectedResponse = "{\"items\": [{\"track\": {\"name\": \"Song 1\"}}]}";

        URL mockUrl = mock(URL.class);
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        InputStream mockInputStream = new ByteArrayInputStream(expectedResponse.getBytes());

        when(mockUrl.openConnection()).thenReturn(mockConnection);
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(mockConnection.getInputStream()).thenReturn(mockInputStream);

        SpotifyServiceImpl serviceSpy = Mockito.spy(spotifyService);
        doReturn(mockUrl).when(serviceSpy).createUrl(anyString());

        // Act
        Map<String, Object> result = serviceSpy.getPlaylistTracks(accessToken, playlistId);
        Map<String, Object> tracksMap = (Map<String, Object>) result.get("tracks");
        List<?> items = (List<?>) tracksMap.get("items");
        Map<String, Object> firstItem = (Map<String, Object>) items.get(0);
        Map<String, Object> track = (Map<String, Object>) firstItem.get("track");
        String trackName = (String) track.get("name");

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("tracks"));
        assertNotNull(tracksMap.get("items"));
        assertTrue(items.size() > 0);
        assertEquals("Song 1", trackName); // Use extracted track name for validation

        verify(mockConnection).setRequestMethod("GET");
        verify(mockConnection).setRequestProperty("Authorization", "Bearer " + accessToken);
        verify(mockConnection).setRequestProperty("Content-Type", "application/json");
    }

    @Test
    void testGetPlaylistTracks_Failure_AuthenticationError() throws Exception {
        // Arrange
        String accessToken = "invalidAccessToken";
        String playlistId = "playlist123";
        String expectedErrorResponse = "{\"error\": {\"status\": 401, \"message\": \"Invalid access token\"}}";

        URL mockUrl = mock(URL.class);
        InputStream mockErrorStream = new ByteArrayInputStream(expectedErrorResponse.getBytes());
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);

        when(mockUrl.openConnection()).thenReturn(mockConnection);
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_UNAUTHORIZED);
        when(mockConnection.getErrorStream()).thenReturn(mockErrorStream);

        SpotifyServiceImpl serviceSpy = Mockito.spy(spotifyService);
        doReturn(mockUrl).when(serviceSpy).createUrl(anyString());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serviceSpy.getPlaylistTracks(accessToken, playlistId);
        });

        assertTrue(exception.getMessage().contains("Spotify API Error: 401"));
    }

    @Test
    void testGetPlaylistTracks_Failure_BadRequest() throws Exception {
        // Arrange
        String accessToken = "validAccessToken";
        String playlistId = "invalidPlaylistId";
        String expectedErrorResponse = "{\"error\": {\"status\": 400, \"message\": \"Bad Request\"}}";

        URL mockUrl = mock(URL.class);
        InputStream mockErrorStream = new ByteArrayInputStream(expectedErrorResponse.getBytes());
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);

        when(mockUrl.openConnection()).thenReturn(mockConnection);
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
        when(mockConnection.getErrorStream()).thenReturn(mockErrorStream);

        SpotifyServiceImpl serviceSpy = Mockito.spy(spotifyService);
        doReturn(mockUrl).when(serviceSpy).createUrl(anyString());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serviceSpy.getPlaylistTracks(accessToken, playlistId);
        });

        assertTrue(exception.getMessage().contains("Spotify API Error: 400"));
    }

    @Test
    void testGetPlaylistTracks_Failure_NetworkError() throws Exception {
        // Arrange
        String accessToken = "validAccessToken";
        String playlistId = "playlist123";

        URL mockUrl = mock(URL.class);
        when(mockUrl.openConnection()).thenThrow(new IOException("Network error"));

        SpotifyServiceImpl serviceSpy = Mockito.spy(spotifyService);
        doReturn(mockUrl).when(serviceSpy).createUrl(anyString());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serviceSpy.getPlaylistTracks(accessToken, playlistId);
        });

        assertTrue(exception.getMessage().contains("Error searching Spotify playlists"));
    }

    @Test
    void testGetPlaylistTracks_Failure_GeneralException() throws Exception {
        // Arrange
        String accessToken = "validAccessToken";
        String playlistId = "playlist123";

        URL mockUrl = mock(URL.class);
        when(mockUrl.openConnection()).thenThrow(new RuntimeException("Unexpected error"));

        SpotifyServiceImpl serviceSpy = Mockito.spy(spotifyService);
        doReturn(mockUrl).when(serviceSpy).createUrl(anyString());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            serviceSpy.getPlaylistTracks(accessToken, playlistId);
        });

        assertTrue(exception.getMessage().contains("Unexpected error"));
    }

}