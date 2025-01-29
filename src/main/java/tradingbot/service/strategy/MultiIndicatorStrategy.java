package tradingbot.service.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tradingbot.model.CoinData;
import tradingbot.model.TradeSignal;
import tradingbot.service.risk.PositionSizingService;
import tradingbot.service.risk.StopLossService;
import tradingbot.service.rule.EntryRuleService;
import tradingbot.service.rule.ExitRuleService;

public class MultiIndicatorStrategy {

    private static final Logger log = LoggerFactory.getLogger(MultiIndicatorStrategy.class);

    private final EntryRuleService entryRuleService;
    private final ExitRuleService exitRuleService;
    private final PositionSizingService positionSizingService;
    private final StopLossService stopLossService;

    public MultiIndicatorStrategy(EntryRuleService entryRuleService,
            ExitRuleService exitRuleService,
            PositionSizingService positionSizingService,
            StopLossService stopLossService) {
        this.entryRuleService = entryRuleService;
        this.exitRuleService = exitRuleService;
        this.positionSizingService = positionSizingService;
        this.stopLossService = stopLossService;
    }

    public TradeSignal evaluate(CoinData coinData) {
        if (coinData == null) {
            log.warn("Received null CoinData");
            return TradeSignal.NO_SIGNAL;
        }

        // Temporary simplified logic for testing
        // TODO:MCA
        if (coinData.getPrice() > coinData.getMovingAverage(200)) {
            return new TradeSignal("BUY", 1000.0, 3, 50.0);
        }
        return TradeSignal.NO_SIGNAL;
    }
}
