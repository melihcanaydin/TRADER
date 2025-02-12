package tradingbot.service.strategy;

import java.util.List;

import org.springframework.stereotype.Service;

import tradingbot.model.MarketData;
import tradingbot.service.analysis.MarketAnalysisHelper;
import tradingbot.service.analysis.MarketIndicators;
import tradingbot.service.rule.LongTermEntryRuleService;
import tradingbot.service.rule.LongTermExitRuleService;

@Service
public class TradingStrategyService {

    private final MarketAnalysisHelper analysisHelper;
    private final LongTermEntryRuleService entryRuleService;
    private final LongTermExitRuleService exitRuleService;

    public TradingStrategyService(MarketAnalysisHelper analysisHelper,
            LongTermEntryRuleService entryRuleService, LongTermExitRuleService exitRuleService) {
        this.analysisHelper = analysisHelper;
        this.entryRuleService = entryRuleService;
        this.exitRuleService = exitRuleService;
    }

    public TradeDecision evaluateTrade(List<MarketData> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Market data cannot be null or empty.");
        }

        MarketIndicators indicators = analysisHelper.calculateIndicators(data);

        boolean shouldEnter = entryRuleService.checkEntryRules(indicators);
        boolean shouldExit = exitRuleService.checkExitRules(indicators);

        if (shouldEnter) {
            shouldExit = false;
        } else if (shouldExit) {
            shouldEnter = false;
        }

        return new TradeDecision(shouldEnter, shouldExit, indicators);
    }

    public static class TradeDecision {
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
}
