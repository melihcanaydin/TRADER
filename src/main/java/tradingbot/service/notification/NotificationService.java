package tradingbot.service.notification;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import tradingbot.config.EnvLoader;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    private final String botToken;
    private final String chatId;
    private static final String TELEGRAM_API_URL = "https://api.telegram.org/bot";

    public NotificationService() {
        this.botToken = EnvLoader.get("TELEGRAM_BOT_TOKEN");
        this.chatId = EnvLoader.get("TELEGRAM_CHAT_ID");
    }

    @PostConstruct
    public void init() {
        log.info("📢 NotificationService initialized!");
        log.info("✅ Telegram Bot is ready to send messages!");
    }


    public void sendMessage(String message) {
        if (botToken == null || chatId == null || botToken.isEmpty() || chatId.isEmpty()) {
            log.error("❌ Telegram bot token or chat ID is missing! Check your .env file.");
            return;
        }

        try {
            // ✅ Properly formatted message with HTML box
            String formattedMessage = "<pre>\n" + message + "\n</pre>";

            // ✅ Telegram API URL for sending messages
            String url = TELEGRAM_API_URL + botToken + "/sendMessage";

            // ✅ Prepare JSON request body
            String requestBody = String.format(
                    "{\"chat_id\": \"%s\", \"text\": \"%s\", \"parse_mode\": \"HTML\"}", chatId,
                    formattedMessage);

            // ✅ Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            // ✅ Create an HTTP entity with headers and body
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            // ✅ Send POST request to Telegram API
            ResponseEntity<String> response =
                    restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            log.info("📤 Telegram message sent successfully! Response: {}", response.getBody());

        } catch (Exception e) {
            log.error("❌ Failed to send Telegram message: {}", e.getMessage(), e);
        }
    }
}
