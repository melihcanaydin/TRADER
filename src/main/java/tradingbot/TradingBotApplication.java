package tradingbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import tradingbot.config.EnvConfigPropertySource;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "tradingbot.repository")
@EntityScan(basePackages = "tradingbot.model")
@EnableScheduling
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
