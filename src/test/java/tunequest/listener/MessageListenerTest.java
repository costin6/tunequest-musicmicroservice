package tunequest.listener;

import jakarta.jms.TextMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import tunequest.business.FavoritePlaylistService;
import tunequest.configuration.TestConfig;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = {TestConfig.class})
@ContextConfiguration(classes = {TestConfig.class})
public class MessageListenerTest {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private FavoritePlaylistService favoritePlaylistService;

    @Test
    void testOnAddMessage() throws Exception {
        String userId = "12345";
        String messageContent = "add" + userId;

        // Send a message to the queue
        jmsTemplate.send("user-login-queue", session -> session.createTextMessage(messageContent));

        // Verify the playlist is saved
        verify(favoritePlaylistService, times(1)).saveDefaultPlaylist(userId);
    }

    @Test
    void testOnDeleteMessage() throws Exception {
        String userId = "12345";
        String messageContent = "del" + userId;

        // Send a message to the queue
        jmsTemplate.send("user-login-queue", session -> session.createTextMessage(messageContent));

        // Verify the playlist is deleted
        verify(favoritePlaylistService, times(1)).deleteUserData(userId);
    }
}
