package tradingbot.service.trading;

import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import tradingbot.model.Coin;
import tradingbot.model.MarketData;
import tradingbot.service.marketdata.MarketDataService;

@Component
@DisallowConcurrentExecution
public class PriceCheckerJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(PriceCheckerJob.class);

    @Autowired
    private MarketDataService marketDataService;
    @Autowired
    private TradingExecutorService tradingExecutor;

    public PriceCheckerJob() {}

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (context == null) {
            logger.error("⚠️ JobExecutionContext is null! This job should only be run by Quartz.");
            return;
        }

        logger.info("✅ PriceCheckerJob executed at: {}", System.currentTimeMillis());


        String strategyType = "long_term"; // Change to "mid_term" or "short_term" dynamically if
                                           // needed.

        for (Coin coin : Coin.values()) {
            try {
                List<MarketData> marketDataList = marketDataService.getMarketData(coin);
                tradingExecutor.executeTradingCheck(coin, marketDataList, strategyType);
            } catch (Exception e) {
                logger.error("❌ Error processing {}: {}", coin.name(), e.getMessage(), e);
            }
        }
    }
}
