package tradingbot.service.rule;

import java.util.List;
import org.springframework.stereotype.Service;
import tradingbot.model.MarketData;
import tradingbot.service.indicator.*;

@Service
public class EntryRuleService {

    private final MovingAverageService maService;
    private final RSIService rsiService;
    private final BollingerBandsService bbService;
    private final MACDService macdService;
    private final OBVService obvService;

    public EntryRuleService(MovingAverageService maService,
            RSIService rsiService,
            BollingerBandsService bbService,
            MACDService macdService,
            OBVService obvService) {
        this.maService = maService;
        this.rsiService = rsiService;
        this.bbService = bbService;
        this.macdService = macdService;
        this.obvService = obvService;
    }

    public Boolean checkEntryRules(List<MarketData> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Market data cannot be null or empty.");
        }

        // 1. Trend: 20-day MA > 50-day MA AND price > 200-day MA
        Double ma20 = maService.calculateSMA(data, 20);
        Double ma50 = maService.calculateSMA(data, 50);
        Double ma200 = maService.calculateSMA(data, 200);
        double currentPrice = data.get(data.size() - 1).getClosePrice();
        boolean trendValid = (ma20 != null && ma50 != null && ma200 != null)
                && (ma20 > ma50) && (currentPrice > ma200);

        // 2. Momentum: RSI(14) < 35 AND rising
        Double rsi = rsiService.calculateRSI(data, 14);
        boolean rsiValid = (rsi != null)
                && (rsi < 35)
                && rsiService.isRisingRSI(data, 14);

        // 3. Volatility: Price at/piercing lower Bollinger Band
        double[] bollingerBands = bbService.calculateBollingerBands(data);
        boolean volatilityValid = (bollingerBands != null)
                && (currentPrice <= bollingerBands[1]); // Lower Bollinger Band

        // 4. Confirmation: MACD bullish crossover + rising OBV
        double[] macd = macdService.calculateMACD(data);
        boolean macdBullish = (macd != null)
                && (macd[0] > macd[1]); // MACD line > Signal line

        Double obv = obvService.calculateOBV(data);
        boolean obvRising = (obv != null)
                && obvService.isRisingOBV(data);

        boolean confirmationValid = macdBullish && obvRising;

        // Combine all entry conditions
        return trendValid && rsiValid && volatilityValid && confirmationValid;
    }
}
