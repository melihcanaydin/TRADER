package tradingbot.service.indicator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import tradingbot.model.MarketData;

@Service
public class RSIService {

    private static final Logger log = LoggerFactory.getLogger(RSIService.class);

    public Double calculateRSI(List<MarketData> data, int period) {
        if (data == null || data.size() < period + 1) {
            log.warn("Not enough data for RSI calculation.");
            return null;
        }
        double[] gains = new double[data.size() - 1];
        double[] losses = new double[data.size() - 1];
        for (int i = 1; i < data.size(); i++) {
            double change = data.get(i).getClosePrice() - data.get(i - 1).getClosePrice();
            gains[i - 1] = Math.max(change, 0);
            losses[i - 1] = Math.max(-change, 0);
        }
        double avgGain = average(gains, period);
        double avgLoss = average(losses, period);
        if (avgLoss == 0) {
            return 100.0;
        }
        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }

    private double average(double[] values, int period) {
        double sum = 0;
        for (int i = 0; i < period; i++) {
            sum += values[i];
        }
        return sum / period;
    }
}
