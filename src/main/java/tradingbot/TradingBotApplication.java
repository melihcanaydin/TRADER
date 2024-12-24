package tradingbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;

import tradingbot.config.EnvConfigPropertySource;

@SpringBootApplication
public class TradingBotApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(TradingBotApplication.class);
        application.addInitializers(context -> {
            ConfigurableEnvironment environment = context.getEnvironment();
            environment.getPropertySources().addFirst(new EnvConfigPropertySource("envConfig"));
        });
        application.run(args);
    }
}
