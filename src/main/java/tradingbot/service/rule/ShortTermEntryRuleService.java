package tradingbot.service.rule;

import org.springframework.stereotype.Service;
import tradingbot.service.analysis.MarketIndicators;

@Service
public class ShortTermEntryRuleService {

    public boolean checkEntryRules(MarketIndicators indicators) {
        // TODO: Implement Short-Term Entry logic (e.g., Bollinger Bands breakout)
        return false;
    }
}
