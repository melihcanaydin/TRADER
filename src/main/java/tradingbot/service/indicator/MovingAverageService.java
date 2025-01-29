package tradingbot.service.indicator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import tradingbot.model.MarketData;

@Service
public class MovingAverageService {

    private static final Logger log = LoggerFactory.getLogger(MovingAverageService.class);

    public Double calculateSMA(List<MarketData> data, int period) {
        if (data == null || data.size() < period) {
            log.warn("Not enough data for SMA calculation.");
            return null;
        }
        double sum = data.stream().skip(data.size() - period).mapToDouble(MarketData::getClosePrice)
                .sum();
        return sum / period;
    }

    public Double calculateEMA(List<MarketData> data, int period) {
        if (data == null || data.size() < period) {
            log.warn("Not enough data for EMA calculation.");
            return null;
        }
        double multiplier = 2.0 / (period + 1);
        double ema = calculateSMA(data.subList(0, period), period);
        for (int i = period; i < data.size(); i++) {
            ema = (data.get(i).getClosePrice() - ema) * multiplier + ema;
        }
        return ema;
    }
}
