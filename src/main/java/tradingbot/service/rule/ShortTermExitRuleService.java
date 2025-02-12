package tradingbot.service.rule;

import org.springframework.stereotype.Service;
import tradingbot.service.analysis.MarketIndicators;

@Service
public class ShortTermExitRuleService {

    public boolean checkExitRules(MarketIndicators indicators) {
        // TODO: Implement Short-Term Exit logic (e.g., stochastic RSI divergence)
        return false;
    }
}
