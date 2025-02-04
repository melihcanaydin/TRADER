package tradingbot.service.strategy;

import java.util.List;

import org.springframework.stereotype.Service;

import tradingbot.model.MarketData;
import tradingbot.service.analysis.MarketAnalysisHelper;
import tradingbot.service.analysis.MarketIndicators;
import tradingbot.service.rule.EntryRuleService;
import tradingbot.service.rule.ExitRuleService;

@Service
public class TradingStrategyService {

    private final MarketAnalysisHelper analysisHelper;
    private final EntryRuleService entryRuleService;
    private final ExitRuleService exitRuleService;

    public TradingStrategyService(MarketAnalysisHelper analysisHelper,
            EntryRuleService entryRuleService, ExitRuleService exitRuleService) {
        this.analysisHelper = analysisHelper;
        this.entryRuleService = entryRuleService;
        this.exitRuleService = exitRuleService;
    }

    public TradeDecision evaluateTrade(List<MarketData> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Market data cannot be null or empty.");
        }

        // ✅ Compute indicators **once** and reuse them
        MarketIndicators indicators = analysisHelper.calculateIndicators(data);

        boolean shouldEnter = entryRuleService.checkEntryRules(indicators);
        boolean shouldExit = exitRuleService.checkExitRules(indicators);

        // ✅ Ensuring that both cannot be true at the same time
        if (shouldEnter) {
            shouldExit = false; // If we have an entry signal, we cannot exit.
        } else if (shouldExit) {
            shouldEnter = false; // If we have an exit signal, we cannot enter.
        }

        return new TradeDecision(shouldEnter, shouldExit, indicators);
    }

    // ✅ Wrapper class to store trade decisions
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
