package tradingbot.service.notification;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.chat.id}")
    private String chatId;

    @PostConstruct
    public void init() {
        log.info("📢 NotificationService initialized!");
        log.info("✅ Telegram Bot Token: {}", botToken);
        log.info("✅ Telegram Chat ID: {}", chatId);
    }

    public void sendMessage(String message) {
        if (botToken == null || chatId == null) {
            log.error("❌ Telegram bot token or chat ID is NULL! Check application.properties.");
            return;
        }

        // ✅ Wrap message inside a rectangle box using HTML preformatted text
        String formattedMessage = "<pre>\n" + message + "\n</pre>";

        String url = String.format(
                "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s&parse_mode=HTML",
                botToken, chatId, formattedMessage);

        try {
            log.info("📤 Sending message to Telegram: {}", formattedMessage);
            restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            log.error("❌ Failed to send Telegram message: {}", e.getMessage());
        }
    }
}
