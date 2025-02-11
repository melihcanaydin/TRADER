package tradingbot.service.trading;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PriceCheckerService {

    private static final Logger logger = LoggerFactory.getLogger(PriceCheckerService.class);

    @Autowired
    private Scheduler scheduler;

    public void run() {
        try {
            JobDetail jobDetail = JobBuilder.newJob(PriceCheckerJob.class)
                    .withIdentity("priceCheckerJob").storeDurably().build();

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity("priceCheckerTrigger")
                    .startNow().build();

            scheduler.scheduleJob(jobDetail, trigger);
            logger.info("✅ Triggered Quartz job for PriceCheckerJob");

        } catch (SchedulerException e) {
            logger.error("❌ Error triggering Quartz job: {}", e.getMessage(), e);
        }
    }
}
