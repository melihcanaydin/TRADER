package tradingbot.service.trading;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tradingbot.model.MarketData;
import tradingbot.model.TradeDecision;
import tradingbot.service.analysis.MarketAnalysisHelper;
import tradingbot.service.analysis.MarketIndicators;
import tradingbot.service.rule.LongTermEntryRuleService;
import tradingbot.service.rule.LongTermExitRuleService;

@Service
public class LongTermStrategy implements TradingStrategy {

    private final LongTermEntryRuleService entryRuleService;
    private final LongTermExitRuleService exitRuleService;
    private final MarketAnalysisHelper marketAnalysisHelper;

    @Autowired
    public LongTermStrategy(LongTermEntryRuleService entryRuleService,
            LongTermExitRuleService exitRuleService, MarketAnalysisHelper marketAnalysisHelper) {
        this.entryRuleService = entryRuleService;
        this.exitRuleService = exitRuleService;
        this.marketAnalysisHelper = marketAnalysisHelper;
    }

    @Override
    public TradeDecision evaluateTrade(List<MarketData> marketData) {
        MarketIndicators indicators = marketAnalysisHelper.calculateIndicators(marketData);

        boolean shouldEnterTrade = entryRuleService.checkEntryRules(indicators);
        boolean shouldExitTrade = exitRuleService.checkExitRules(indicators);

        return new TradeDecision(shouldEnterTrade, shouldExitTrade, indicators);
    }
}
