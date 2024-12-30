package tradingbot.rules;

import tradingbot.model.CoinData;
import java.util.ArrayList;
import java.util.List;

public class RuleEngine {

    private final List<Rule> rules = new ArrayList<>();

    public void registerRule(Rule rule) {
        rules.add(rule);
    }

    public List<Rule> getMatchingRules(CoinData coinData) {
        List<Rule> matchingRules = new ArrayList<>();
        for (Rule rule : rules) {
            if (rule.check(coinData)) {
                matchingRules.add(rule);
            }
        }
        return matchingRules;
    }
}
