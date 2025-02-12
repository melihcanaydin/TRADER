package tradingbot.service.trading;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tradingbot.model.MarketData;
import tradingbot.model.TradeDecision;
import tradingbot.service.analysis.MarketAnalysisHelper;
import tradingbot.service.analysis.MarketIndicators;
import tradingbot.service.rule.ShortTermEntryRuleService;
import tradingbot.service.rule.ShortTermExitRuleService;

@Service
public class ShortTermStrategy implements TradingStrategy {

    private final ShortTermEntryRuleService entryRuleService;
    private final ShortTermExitRuleService exitRuleService;
    private final MarketAnalysisHelper marketAnalysisHelper;

    @Autowired
    public ShortTermStrategy(ShortTermEntryRuleService entryRuleService,
            ShortTermExitRuleService exitRuleService, MarketAnalysisHelper marketAnalysisHelper) {
        this.entryRuleService = entryRuleService;
        this.exitRuleService = exitRuleService;
        this.marketAnalysisHelper = marketAnalysisHelper;
    }

    @Override
    public TradeDecision evaluateTrade(List<MarketData> marketData) {
        if (marketData.isEmpty()) {
            return new TradeDecision(false, false, null);
        }

        MarketIndicators indicators = marketAnalysisHelper.calculateIndicators(marketData);

        boolean shouldEnterTrade = entryRuleService.checkEntryRules(indicators);
        boolean shouldExitTrade = exitRuleService.checkExitRules(indicators);

        return new TradeDecision(shouldEnterTrade, shouldExitTrade, indicators);
    }
}
