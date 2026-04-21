package tunequest.business.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import tunequest.business.SpotifyService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class SpotifyServiceImpl implements SpotifyService {

    protected URL createUrl(String urlString) throws MalformedURLException {
        return new URL(urlString);
    }

    @Override
    public Map<String, Object> searchPlaylists(String accessToken, String query) {
        try {
            String urlString = String.format(
                    "https://api.spotify.com/v1/search?q=%s&type=playlist",
                    URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
            );

            URL url = createUrl(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();

            BufferedReader reader = responseCode >= 200 && responseCode <= 299
                    ? new BufferedReader(new InputStreamReader(connection.getInputStream()))
                    : new BufferedReader(new InputStreamReader(connection.getErrorStream()));

            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> responseMap = mapper.readValue(response.toString(), Map.class);

                Map<String, Object> result = new HashMap<>();
                result.put("playlists", responseMap.get("playlists"));

                return result;
            } else {
                throw new SpotifyApiException("Spotify API Error: " + responseCode + " - " + response);
            }

        } catch (MalformedURLException e) {
            throw new SpotifyServiceException("Invalid URL format while constructing the Spotify API URL", e);
        } catch (IOException e) {
            throw new SpotifyServiceException("Error during communication with Spotify API", e);
        } catch (SpotifyApiException e) {
            throw e;
        } catch (Exception e) {
            throw new SpotifyServiceException("Unexpected error occurred while searching playlists", e);
        }
    }

    @Override
    public Map<String, Object> getPlaylistTracks(String accessToken, String playlistId) {
        try {
            String urlString = String.format(
                    "https://api.spotify.com/v1/playlists/%s/tracks",
                    URLEncoder.encode(playlistId, StandardCharsets.UTF_8.toString())
            );

            URL url = createUrl(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();

            BufferedReader reader = responseCode >= 200 && responseCode <= 299
                    ? new BufferedReader(new InputStreamReader(connection.getInputStream()))
                    : new BufferedReader(new InputStreamReader(connection.getErrorStream()));

            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> responseMap = mapper.readValue(response.toString(), Map.class);

                // Extract and prepare tracks
                Map<String, Object> result = new HashMap<>();
                result.put("tracks", responseMap);

                return result;
            } else {
                throw new SpotifyApiException("Spotify API Error: " + responseCode + " - " + response);
            }

        } catch (MalformedURLException e) {
            throw new SpotifyServiceException("Invalid URL format while constructing the Spotify API URL", e);
        } catch (IOException e) {
            throw new SpotifyServiceException("Error during communication with Spotify API", e);
        } catch (SpotifyApiException e) {
            throw e;
        } catch (Exception e) {
            throw new SpotifyServiceException("Unexpected error occurred while fetching playlist tracks", e);
        }
    }

    // Custom exceptions
    public static class SpotifyApiException extends RuntimeException {
        public SpotifyApiException(String message) {
            super(message);
        }
    }

    public static class SpotifyServiceException extends RuntimeException {
        public SpotifyServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
