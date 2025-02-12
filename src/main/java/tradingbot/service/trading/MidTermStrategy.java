package tradingbot.service.trading;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tradingbot.model.MarketData;
import tradingbot.model.TradeDecision;
import tradingbot.service.analysis.MarketAnalysisHelper;
import tradingbot.service.analysis.MarketIndicators;
import tradingbot.service.rule.MidTermEntryRuleService;
import tradingbot.service.rule.MidTermExitRuleService;

@Service
public class MidTermStrategy implements TradingStrategy {

    private final MidTermEntryRuleService entryRuleService;
    private final MidTermExitRuleService exitRuleService;
    private final MarketAnalysisHelper marketAnalysisHelper;

    @Autowired
    public MidTermStrategy(MidTermEntryRuleService entryRuleService,
            MidTermExitRuleService exitRuleService, MarketAnalysisHelper marketAnalysisHelper) {
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
