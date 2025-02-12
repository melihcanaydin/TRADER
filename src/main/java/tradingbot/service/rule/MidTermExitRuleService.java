package tradingbot.service.rule;

import org.springframework.stereotype.Service;
import tradingbot.service.analysis.MarketIndicators;

@Service
public class MidTermExitRuleService {

    public boolean checkExitRules(MarketIndicators indicators) {
        // TODO: Implement Mid-Term Exit logic (e.g., RSI overbought conditions)
        return false;
    }
}
