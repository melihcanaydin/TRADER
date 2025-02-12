package tradingbot.service.trading;

import java.util.List;

import tradingbot.model.MarketData;
import tradingbot.model.TradeDecision;

public interface TradingStrategy {
    TradeDecision evaluateTrade(List<MarketData> marketData);
}
