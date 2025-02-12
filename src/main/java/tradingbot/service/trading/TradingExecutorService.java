package tradingbot.service.trading;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tradingbot.model.Coin;
import tradingbot.model.MarketData;
import tradingbot.model.TradeDecision;
import tradingbot.service.notification.NotificationService;

@Service
public class TradingExecutorService {

    private final TradingStrategyFactory strategyFactory;
    private final NotificationService telegramService;

    @Autowired
    public TradingExecutorService(TradingStrategyFactory strategyFactory,
            NotificationService telegramService) {
        this.strategyFactory = strategyFactory;
        this.telegramService = telegramService;
    }

    public void executeTradingCheck(Coin coin, List<MarketData> marketDataList,
            String strategyType) {
        if (marketDataList == null || marketDataList.isEmpty()) {
            return;
        }

        TradingStrategy strategy = strategyFactory.getStrategy(strategyType);
        TradeDecision tradeDecision = strategy.evaluateTrade(marketDataList);

        MarketData latestData = marketDataList.get(0);
        if (tradeDecision.shouldEnterTrade) {
            sendBuySignal(latestData, tradeDecision, strategyType);
        }

        if (tradeDecision.shouldExitTrade) {
            sendSellSignal(latestData, tradeDecision, strategyType);
        }
    }

    private void sendBuySignal(MarketData latestData, TradeDecision tradeDecision,
            String strategyType) {
        String buyMessage = String.format("<pre>\n" + "<b>📊 Strategy Type: %s</b>\n" + // Added
                                                                                        // StrategyType
                                                                                        // here
                "<b>🚀 BUY SIGNAL DETECTED! 🚀</b>\n" + "<b>🔹 Coin:</b> %s\n"
                + "<b>💰 Price:</b> <code>%.2f</code>\n" + "<b>📊 RSI (14):</b> <code>%.2f</code>\n"
                + "<b>📈 MACD Line:</b> <code>%.2f</code>\n"
                + "<b>🔼 Bollinger Upper:</b> <code>%.2f</code>\n"
                + "<b>📈 OBV:</b> <code>%.2f</code>\n" + "</pre>", strategyType, // ✅ Pass
                                                                                 // StrategyType
                                                                                 // dynamically
                latestData.getSymbol(), latestData.getClosePrice(), tradeDecision.indicators.rsi,
                tradeDecision.indicators.macdLine, tradeDecision.indicators.bollingerBands[0],
                tradeDecision.indicators.obv);

        telegramService.sendMessage(buyMessage);
    }

    private void sendSellSignal(MarketData latestData, TradeDecision tradeDecision,
            String strategyType) {
        String sellMessage = String.format("<pre>\n" + "<b>📊 Strategy Type: %s</b>\n" + // Added
                                                                                         // StrategyType
                                                                                         // here
                "<b>⚠️ SELL SIGNAL DETECTED! ⚠️</b>\n" + "<b>🔹 Coin:</b> %s\n"
                + "<b>💰 Price:</b> <code>%.2f</code>\n" + "<b>📊 RSI (14):</b> <code>%.2f</code>\n"
                + "<b>📉 MACD Line:</b> <code>%.2f</code>\n"
                + "<b>🔽 Bollinger Lower:</b> <code>%.2f</code>\n"
                + "<b>📉 OBV:</b> <code>%.2f</code>\n" + "</pre>", strategyType, // ✅ Pass
                                                                                 // StrategyType
                                                                                 // dynamically
                latestData.getSymbol(), latestData.getClosePrice(), tradeDecision.indicators.rsi,
                tradeDecision.indicators.macdLine, tradeDecision.indicators.bollingerBands[2],
                tradeDecision.indicators.obv);

        telegramService.sendMessage(sellMessage);
    }
}
