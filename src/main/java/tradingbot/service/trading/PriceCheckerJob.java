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
import tradingbot.service.notification.NotificationService;
import tradingbot.service.strategy.TradingStrategyService;
import tradingbot.service.strategy.TradingStrategyService.TradeDecision;

@Component
@DisallowConcurrentExecution
public class PriceCheckerJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(PriceCheckerJob.class);

    @Autowired
    private MarketDataService marketDataService;
    @Autowired
    private TradingStrategyService tradingStrategy;
    @Autowired
    private NotificationService telegramService;

    public PriceCheckerJob() {}

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (context == null) {
            logger.error("⚠️ JobExecutionContext is null! This job should only be run by Quartz.");
            return;
        }

        logger.info("✅ PriceCheckerJob executed at: {}", System.currentTimeMillis());
        logger.info("Job key: {}", context.getJobDetail().getKey());
        logger.info("Next fire time: {}", context.getTrigger().getNextFireTime());

        processTradingLogic();
    }

    private void processTradingLogic() {
        for (Coin coin : Coin.values()) {
            try {
                List<MarketData> marketDataList = marketDataService.getMarketData(coin);
                if (marketDataList == null || marketDataList.isEmpty()) {
                    logger.warn("Skipping {} due to missing market data", coin.name());
                    continue;
                }

                TradeDecision tradeDecision = tradingStrategy.evaluateTrade(marketDataList);
                MarketData latestData = marketDataList.get(0);

                if (tradeDecision.shouldEnterTrade) {
                    handleBuySignal(latestData, tradeDecision);
                }

                if (tradeDecision.shouldExitTrade) {
                    handleSellSignal(latestData, tradeDecision);
                }

            } catch (Exception e) {
                logger.error("❌ Error processing {}: {}", coin.name(), e.getMessage(), e);
            }
        }
    }

    private void handleBuySignal(MarketData latestData, TradeDecision tradeDecision) {
        String buyMessage = String.format(
                "<pre>\n" + "<b>🚀 BUY SIGNAL DETECTED! 🚀</b>\n" + "<b>🔹 Coin:</b> %s\n"
                        + "<b>💰 Price:</b> <code>%.2f</code>\n"
                        + "<b>📊 RSI (14):</b> <code>%.2f</code>\n"
                        + "<b>📈 MACD Line:</b> <code>%.2f</code>\n"
                        + "<b>🔼 Bollinger Upper:</b> <code>%.2f</code>\n"
                        + "<b>📈 OBV:</b> <code>%.2f</code>\n" + "</pre>",
                latestData.getSymbol(), latestData.getClosePrice(), tradeDecision.indicators.rsi,
                tradeDecision.indicators.macdLine, tradeDecision.indicators.bollingerBands[0],
                tradeDecision.indicators.obv);

        logger.info(buyMessage);
        // telegramService.sendMessage(buyMessage);
    }

    private void handleSellSignal(MarketData latestData, TradeDecision tradeDecision) {
        String sellMessage = String.format(
                "<pre>\n" + "<b>⚠️ SELL SIGNAL DETECTED! ⚠️</b>\n" + "<b>🔹 Coin:</b> %s\n"
                        + "<b>💰 Price:</b> <code>%.2f</code>\n"
                        + "<b>📊 RSI (14):</b> <code>%.2f</code>\n"
                        + "<b>📉 MACD Line:</b> <code>%.2f</code>\n"
                        + "<b>🔽 Bollinger Lower:</b> <code>%.2f</code>\n"
                        + "<b>📉 OBV:</b> <code>%.2f</code>\n" + "</pre>",
                latestData.getSymbol(), latestData.getClosePrice(), tradeDecision.indicators.rsi,
                tradeDecision.indicators.macdLine, tradeDecision.indicators.bollingerBands[2],
                tradeDecision.indicators.obv);

        logger.info(sellMessage);
        // telegramService.sendMessage(sellMessage);
    }
}
