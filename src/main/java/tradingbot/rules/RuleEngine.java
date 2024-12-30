package tradingbot.rules;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import tradingbot.model.CoinData;

@Component
public class RuleEngine {

    private final List<Rule> rules = new ArrayList<>();

    public void registerRule(Rule rule) {
        rules.add(rule);
    }

    public List<Rule> getMatchingRules(CoinData coinData) {
        List<Rule> matchedRules = new ArrayList<>();
        for (Rule rule : rules) {
            if (rule.check(coinData)) {
                matchedRules.add(rule);
            }
        }
        return matchedRules;
    }
}
