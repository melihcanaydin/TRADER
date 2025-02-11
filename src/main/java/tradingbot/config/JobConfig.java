package tradingbot.config;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tradingbot.service.trading.PriceCheckerJob;

@Configuration
public class JobConfig {

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob(PriceCheckerJob.class).withIdentity("priceCheckerJob")
                .storeDurably().build();
    }

    @Bean
    public Trigger jobTrigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger().forJob(jobDetail).withIdentity("priceCheckerTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(60)
                        .repeatForever())
                .build();
    }
}
