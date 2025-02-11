package tradingbot;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
public class TradingBotApplication implements CommandLineRunner {
    @Autowired
    private Scheduler scheduler;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(TradingBotApplication.class);
        application.addInitializers(context -> {
            ConfigurableEnvironment environment = context.getEnvironment();
            environment.getPropertySources().addFirst(new EnvConfigPropertySource("envConfig"));
        });

        application.run(args);
    }

    @Override
    public void run(String... args) throws SchedulerException {
        scheduler.triggerJob(new JobKey("priceCheckerJob"));
    }
}
