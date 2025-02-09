package tradingbot.service.trading;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import tradingbot.model.BuySuggestion;
import tradingbot.model.Coin;
import tradingbot.model.MarketData;
import tradingbot.repository.BuySuggestionRepository;
import tradingbot.repository.LogicRepository;
import tradingbot.service.marketdata.MarketDataService;
import tradingbot.service.notification.NotificationService;
import tradingbot.service.strategy.TradingStrategyService;
import tradingbot.service.strategy.TradingStrategyService.TradeDecision;

@Service
public class PriceCheckerService {

    private static final Logger logger = LoggerFactory.getLogger(PriceCheckerService.class);
    private final LogicRepository logicRepository;
    private final MarketDataService marketDataService;
    private final BuySuggestionRepository buySuggestionRepository;
    private final NotificationService telegramService;
    private final TradingStrategyService tradingStrategy;

    public PriceCheckerService(LogicRepository logicRepository, MarketDataService marketDataService,
            BuySuggestionRepository buySuggestionRepository, NotificationService telegramService,
            TradingStrategyService tradingStrategy) {
        this.logicRepository = logicRepository;
        this.marketDataService = marketDataService;
        this.buySuggestionRepository = buySuggestionRepository;
        this.tradingStrategy = tradingStrategy;
        this.telegramService = telegramService;
    }

    private static final Logger log = LoggerFactory.getLogger(PriceCheckerService.class);

    @Scheduled(fixedRate = 6000000) // 1 hour
    public void checkPrices() {
        for (Coin coin : Coin.values()) {
            try {
                // ✅ Fetch historical market data
                List<MarketData> marketDataList = marketDataService.getMarketData(coin);

                if (marketDataList == null || marketDataList.isEmpty()) {
                    logger.warn("Skipping {} due to missing market data", coin.name());
                    continue;
                }

                // ✅ Evaluate trade decisions
                TradeDecision tradeDecision = tradingStrategy.evaluateTrade(marketDataList);
                MarketData latestData = marketDataList.get(0); // Latest candle

                if (tradeDecision.shouldEnterTrade) {
                    handleBuySignal(latestData, tradeDecision);
                }

                if (tradeDecision.shouldExitTrade) {
                    handleSellSignal(latestData, tradeDecision);
                }

            } catch (Exception e) {
                logger.error("❌ Failed to process {}: {}", coin.name(), e.getMessage(), e);
            }
        }
    }

    private void handleBuySignal(MarketData latestData, TradeDecision tradeDecision) {
        BuySuggestion suggestion = new BuySuggestion(latestData.getSymbol(),
                latestData.getClosePrice(), tradeDecision.indicators.rsi,
                tradeDecision.indicators.macdLine, tradeDecision.indicators.bollingerBands[0] // Upper
                                                                                              // BB
        );

        buySuggestionRepository.save(suggestion);
        logger.info("💹 Buy suggestion saved for {}", latestData.getSymbol());

        // ✅ Generate Buy Signal Message in HTML format
        String buyMessage = String.format("<pre>\n" + "<b>🚀 BUY SIGNAL DETECTED! 🚀</b>\n"
                + "<b>🔹 Coin:</b> %s\n" + "<b>💰 Price:</b> <code>%.2f</code>\n"
                + "<b>📊 RSI (14):</b> <code>%.2f</code>\n"
                + "<b>📈 MACD Line:</b> <code>%.2f</code>\n"
                + "<b>🔼 Bollinger Upper:</b> <code>%.2f</code>\n"
                // + "<b>📏 ATR (14):</b> <code>%.2f</code>\n"
                + "<b>📈 OBV:</b> <code>%.2f</code>\n" + "</pre>", latestData.getSymbol(),
                latestData.getClosePrice(), tradeDecision.indicators.rsi,
                tradeDecision.indicators.macdLine, tradeDecision.indicators.bollingerBands[0],
                /* tradeDecision.indicators.atr, */ tradeDecision.indicators.obv);

        log.info(buyMessage);
        // telegramService.sendMessage(buyMessage);
    }

    private void handleSellSignal(MarketData latestData, TradeDecision tradeDecision) {
        // ✅ Generate Sell Signal Message in HTML format
        String sellMessage = String.format("<pre>\n" + "<b>⚠️ SELL SIGNAL DETECTED! ⚠️</b>\n"
                + "<b>🔹 Coin:</b> %s\n" + "<b>💰 Price:</b> <code>%.2f</code>\n"
                + "<b>📊 RSI (14):</b> <code>%.2f</code>\n"
                + "<b>📉 MACD Line:</b> <code>%.2f</code>\n"
                + "<b>🔽 Bollinger Lower:</b> <code>%.2f</code>\n"
                // + "<b>📏 ATR (14):</b> <code>%.2f</code>\n"
                + "<b>📉 OBV:</b> <code>%.2f</code>\n" + "</pre>", latestData.getSymbol(),
                latestData.getClosePrice(), tradeDecision.indicators.rsi,
                tradeDecision.indicators.macdLine, tradeDecision.indicators.bollingerBands[2],
                /* tradeDecision.indicators.atr, */ tradeDecision.indicators.obv);

        log.info(sellMessage);
        // telegramService.sendMessage(sellMessage);
        logger.info("📉 Sell signal triggered for {}", latestData.getSymbol());
    }
}
