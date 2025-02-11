package tradingbot.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {
    private static final Dotenv dotenv =
            Dotenv.configure().directory(System.getProperty("user.dir")).ignoreIfMissing().load();

    public static String get(String key) {
        return dotenv.get(key, "");
    }
}
