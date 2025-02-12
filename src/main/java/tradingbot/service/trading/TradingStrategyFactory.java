package tradingbot.service.trading;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TradingStrategyFactory {

    private final LongTermStrategy longTermStrategy;
    private final MidTermStrategy midTermStrategy;
    private final ShortTermStrategy shortTermStrategy;

    @Autowired
    public TradingStrategyFactory(LongTermStrategy longTermStrategy,
            MidTermStrategy midTermStrategy, ShortTermStrategy shortTermStrategy) {
        this.longTermStrategy = longTermStrategy;
        this.midTermStrategy = midTermStrategy;
        this.shortTermStrategy = shortTermStrategy;
    }

    public TradingStrategy getStrategy(String strategyType) {
        return switch (strategyType.toLowerCase()) {
            case "long_term" -> longTermStrategy;
            case "mid_term" -> midTermStrategy;
            case "short_term" -> shortTermStrategy;
            default -> throw new IllegalArgumentException("Invalid strategy type: " + strategyType);
        };
    }
}
