package tradingbot.service.indicator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import tradingbot.model.MarketData;

@Service
public class MACDService {

    private static final Logger log = LoggerFactory.getLogger(MACDService.class);

    public double calculateMACD(List<MarketData> data) {
        if (data == null || data.size() < 26) {
            log.warn("Not enough data for MACD calculation.");
            return 0.0;
        }
        double ema12 = calculateEMA(data, 12);
        double ema26 = calculateEMA(data, 26);
        return ema12 - ema26;
    }

    private double calculateEMA(List<MarketData> data, int period) {
        if (data == null || data.size() < period) {
            return 0.0;
        }
        double multiplier = 2.0 / (period + 1);
        double ema = data.get(0).getClosePrice();
        for (int i = 1; i < data.size(); i++) {
            ema = (data.get(i).getClosePrice() - ema) * multiplier + ema;
        }
        return ema;
    }
}
