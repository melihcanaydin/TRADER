package tradingbot.model;

import tradingbot.service.analysis.MarketIndicators;

public class TradeDecision {
    public final boolean shouldEnterTrade;
    public final boolean shouldExitTrade;
    public final MarketIndicators indicators;

    public TradeDecision(boolean shouldEnterTrade, boolean shouldExitTrade,
            MarketIndicators indicators) {
        this.shouldEnterTrade = shouldEnterTrade;
        this.shouldExitTrade = shouldExitTrade;
        this.indicators = indicators;
    }
}
