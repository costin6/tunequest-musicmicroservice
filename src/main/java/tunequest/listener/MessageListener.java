package tunequest.listener;

import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import tunequest.business.FavoritePlaylistService;

@Component
public class MessageListener {

    private final FavoritePlaylistService favoritePlaylistService;

    public MessageListener(FavoritePlaylistService favoritePlaylistService) {
        this.favoritePlaylistService = favoritePlaylistService;
    }

    @JmsListener(destination = "user-login-queue")
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String messageContent = ((TextMessage) message).getText();
                String operation = messageContent.substring(0, 3);
                String id = messageContent.substring(3);

                if(favoritePlaylistService.getUserFavoritePlaylist(id) == null) {
                    if(operation.equals("add")) {
                        favoritePlaylistService.saveDefaultPlaylist(id);
                    }
                }
                else{
                    if (operation.equals("del")) {
                        favoritePlaylistService.deleteUserData(id);
                    }
                }
            } else {
                System.err.println("Received non-text message");
            }
        } catch (Exception e) {
            System.err.println("Failed to process message: " + e.getMessage());
        }
    }
}
