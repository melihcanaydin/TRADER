package tradingbot.service.rule;

import java.util.List;

import org.springframework.stereotype.Service;

import tradingbot.model.MarketData;
import tradingbot.service.indicator.BollingerBandsService;
import tradingbot.service.indicator.MACDService;
import tradingbot.service.indicator.MovingAverageService;
import tradingbot.service.indicator.OBVService;
import tradingbot.service.indicator.RSIService;

@Service
public class ExitRuleService {

    private final MovingAverageService maService;
    private final RSIService rsiService;
    private final BollingerBandsService bbService;
    private final MACDService macdService;
    private final OBVService obvService;

    public ExitRuleService(MovingAverageService maService,
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

    public Boolean checkExitRules(List<MarketData> data) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("Market data cannot be null or empty.");
        }

        // 1. Trend Reversal: 20-day MA < 50-day MA OR price < 200-day MA
        Double ma20 = maService.calculateSMA(data, 20);
        Double ma50 = maService.calculateSMA(data, 50);
        Double ma200 = maService.calculateSMA(data, 200);
        double currentPrice = data.get(data.size() - 1).getClosePrice();
        boolean trendReversal = (ma20 != null && ma50 != null && ma200 != null)
                && (ma20 < ma50 || currentPrice < ma200);

        // 2. Momentum Exhaustion: RSI(14) > 65 AND falling
        Double rsi = rsiService.calculateRSI(data, 14);
        boolean rsiExhausted = (rsi != null)
                && (rsi > 65)
                && !rsiService.isRisingRSI(data, 14); // RSI is not rising

        // 3. Volatility Spike: Price at/piercing upper Bollinger Band
        double[] bollingerBands = bbService.calculateBollingerBands(data);
        boolean volatilitySpike = (bollingerBands != null)
                && (currentPrice >= bollingerBands[0]); // Upper Bollinger Band

        // 4. Confirmation: MACD bearish crossover + falling OBV
        double[] macd = macdService.calculateMACD(data);
        boolean macdBearish = (macd != null)
                && (macd[0] < macd[1]); // MACD line < Signal line

        Double obv = obvService.calculateOBV(data);
        boolean obvFalling = (obv != null)
                && !obvService.isRisingOBV(data); // OBV is not rising

        boolean confirmationValid = macdBearish && obvFalling;

        // Combine all exit conditions
        //TODO: MCA; Put it && maybe
        return trendReversal || rsiExhausted || volatilitySpike || confirmationValid;
    }
}
